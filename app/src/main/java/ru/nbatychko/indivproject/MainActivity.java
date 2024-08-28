package ru.nbatychko.indivproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;
import ru.nbatychko.indivproject.adapters.CoinAdapter;
import ru.nbatychko.indivproject.api.CoinDetail;
import ru.nbatychko.indivproject.api.CoinGeckoApi;
import ru.nbatychko.indivproject.api.MarketChart;
import ru.nbatychko.indivproject.api.NetworkService;
import ru.nbatychko.indivproject.db.CoinModel;
import ru.nbatychko.indivproject.db.DatabaseHelper;
import ru.nbatychko.indivproject.db.Point;
import ru.nbatychko.indivproject.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {
    public List<CoinModel> coins;
    public CoinAdapter coinAdapter;
    private boolean isUpdating = false;
    private boolean isChartsUpdatedToday = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coins = DatabaseHelper.getInstance(getApplicationContext()).getAllCoins();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MainFragment())
                    .commit();
        }
        updateAllCoins();
    }

    public void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        coinAdapter = null;
    }

    public void addCoin(String apiId) {
        new AddTask().execute(apiId);
    }

    /**
     * @noinspection deprecation
     */
    class AddTask extends AsyncTask<String, Void, CoinModel> {
        private final String TAG = AddTask.class.getName();

        @Override
        protected CoinModel doInBackground(String... apiIds) {
            String apiId = apiIds[0];
            CoinGeckoApi api = NetworkService.getInstance().getJSONApi();
            CoinModel newCoin = null;

            Call<List<CoinDetail>> detailCall = api.getMarketDataForCoins(apiId, "usd");
            try {
                Response<List<CoinDetail>> resp = detailCall.execute();
                if (resp.isSuccessful()) {
                    newCoin = new CoinModel(resp.body().get(0));
                } else {
                    Log.e(TAG, "Data HTTP error: " + resp.code());
                }
            } catch (IOException ex) {
                Log.e(TAG, "Data Network error", ex);
            }

            Call<MarketChart> chartCall = api.getMarketChart(apiId, "usd", "30", "daily");
            try {
                Response<MarketChart> resp = chartCall.execute();
                if (resp.isSuccessful()) {
                    List<String[]> prices = resp.body().getPrices();
                    if (!prices.isEmpty()) {
                        prices.remove(prices.size() - 1); // убираем текущую цену
                    }
                    List<Point> points = prices.stream().map(Point::new).collect(Collectors.toList());
                    if (!points.isEmpty()) {
                        newCoin.setChart(points);
                    }
                } else {
                    Log.e(TAG, "Chart HTTP error: " + resp.code());
                }
            } catch (IOException ex) {
                Log.e(TAG, "Chart Network error", ex);
            }

            if (newCoin != null) {
                DatabaseHelper.getInstance(getApplicationContext()).addCoin(newCoin);
            }
            return newCoin;
        }

        @Override
        protected void onPostExecute(CoinModel coinModel) {
            if (coinModel == null) {
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            coins.add(coinModel);
            if (coinAdapter != null) {
                coinAdapter.notifyItemInserted(coins.size() - 1);
            }
            Log.d(TAG, "Finished adding the coin");
        }
    }

    public void updateAllCoins() {
        if (isUpdating || coins.isEmpty()) return;
        isUpdating = true;
        new UpdateTask().execute(coins.toArray(new CoinModel[0]));
    }

    /**
     * @noinspection deprecation
     */
    class UpdateTask extends AsyncTask<CoinModel, Void, Void> {
        private final String TAG = UpdateTask.class.getName();

        @Override
        protected Void doInBackground(CoinModel... coinModels) {
            String ids = Arrays.stream(coinModels).map(CoinModel::getApiId).collect(Collectors.joining(","));
            CoinGeckoApi api = NetworkService.getInstance().getJSONApi();

            Call<List<CoinDetail>> detailsCall = api.getMarketDataForCoins(ids, "usd");
            List<CoinDetail> updatedDetails = null;
            try {
                Response<List<CoinDetail>> resp = detailsCall.execute();
                if (resp.isSuccessful()) {
                    updatedDetails = resp.body();
                } else {
                    Log.e(TAG, "Data HTTP error: " + resp.code());
                }
            } catch (IOException ex) {
                Log.e(TAG, "Data Network error", ex);
            }

            if (updatedDetails == null) return null;
            for (CoinDetail updatedDetail : updatedDetails) {
                CoinModel updatedModel = new CoinModel(updatedDetail);
                if (!isChartsUpdatedToday) {
                    Call<MarketChart> chartCall = api.getMarketChart(updatedModel.getApiId(), "usd", "30", "daily");
                    try {
                        Response<MarketChart> resp = chartCall.execute();
                        if (resp.isSuccessful()) {
                            List<String[]> prices = resp.body().getPrices();
                            if (!prices.isEmpty()) {
                                prices.remove(prices.size() - 1); // убираем текущую цену
                            }
                            List<Point> points = prices.stream().map(Point::new).collect(Collectors.toList());
                            if (!points.isEmpty()) {
                                updatedModel.setChart(points);
                            }
                        } else {
                            Log.e(TAG, "Chart HTTP error: " + resp.code());
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "Chart Network error", ex);
                    }

                    try {
                        Thread.sleep(1000L); // rate limit
                    } catch (InterruptedException ex) {
                        Log.e(TAG, "Sleep interrupted", ex);
                    }
                }
                DatabaseHelper.getInstance(getApplicationContext()).updateCoin(updatedModel);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            coins.clear();
            coins.addAll(DatabaseHelper.getInstance(getApplicationContext()).getAllCoins());
            if (coinAdapter != null) {
                coinAdapter.notifyDataSetChanged();
            }
            isUpdating = false;
            isChartsUpdatedToday = true;
            Log.d(TAG, "Finished updating all coins");
        }
    }
}