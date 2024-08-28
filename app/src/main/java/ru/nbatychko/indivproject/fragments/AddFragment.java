package ru.nbatychko.indivproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nbatychko.indivproject.MainActivity;
import ru.nbatychko.indivproject.R;
import ru.nbatychko.indivproject.adapters.CoinPreviewAdapter;
import ru.nbatychko.indivproject.api.CoinGeckoApi;
import ru.nbatychko.indivproject.api.CoinPreview;
import ru.nbatychko.indivproject.api.NetworkService;
import ru.nbatychko.indivproject.api.SearchResponse;
import ru.nbatychko.indivproject.databinding.FragmentAddBinding;
import ru.nbatychko.indivproject.db.CoinModel;

public class AddFragment extends Fragment {
    private static final String TAG = AddFragment.class.getName();
    private FragmentAddBinding binding;
    private List<CoinPreview> coins;
    private CoinPreviewAdapter previewAdapter;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_add);

        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        Menu menu = binding.toolbar.getMenu();
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CoinGeckoApi api = NetworkService.getInstance().getJSONApi();
                Call<SearchResponse> call = api.searchCoins(query);

                call.enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> resp) {
                        if (resp.isSuccessful()) {
                            List<CoinPreview> newCoins = resp.body().getCoins();

                            Set<String> apiIds = ((MainActivity) getActivity()).coins.stream().map(CoinModel::getApiId).collect(Collectors.toSet());
                            newCoins.removeIf(c -> apiIds.contains(c.getId()));

                            coins.clear();
                            coins.addAll(newCoins);
                            previewAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "HTTP error: " + resp.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Log.e(TAG, "Network error", t);
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void setupList() {
        MainActivity main = (MainActivity) getActivity();
        previewAdapter = new CoinPreviewAdapter(getContext(), coins, coin -> {
            main.addCoin(coin.getId());
            main.switchFragment(new MainFragment());
        });
        binding.list.setAdapter(previewAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coins = new ArrayList<>();
        setupList();
        setupToolbar();
    }
}