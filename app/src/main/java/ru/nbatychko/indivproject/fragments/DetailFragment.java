package ru.nbatychko.indivproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.nbatychko.indivproject.MainActivity;
import ru.nbatychko.indivproject.R;
import ru.nbatychko.indivproject.databinding.FragmentDetailBinding;
import ru.nbatychko.indivproject.db.CoinModel;
import ru.nbatychko.indivproject.db.DatabaseHelper;
import ru.nbatychko.indivproject.db.Point;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    private static final String ARG_INDEX = "index";
    private int mIndex;
    private CoinModel coin;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void setupCoin() {
        binding.toolbar.setTitle(coin.getSymbol());
        Picasso.get().load(coin.getImage()).into(binding.logo);
        binding.name.setText(coin.getName());
        binding.price.setText(coin.getFormattedPrice());
        binding.priceChange.setText(coin.getFormattedPriceChange());
        if (coin.getPriceChange() > 0) {
            binding.priceChange.setTextColor(getResources().getColor(R.color.change_up));
        } else if (coin.getPriceChange() < 0) {
            binding.priceChange.setTextColor(getResources().getColor(R.color.change_down));
        }
        binding.marketCap.setText(coin.getFormattedMarketCap());
    }

    private void setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_detail);
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete) {
                MainActivity main = (MainActivity) getActivity();
                DatabaseHelper.getInstance(main.getApplicationContext()).deleteCoin(coin.getId());
                main.coins.remove(mIndex);
                main.switchFragment(new MainFragment());
                return true;
            }
            return false;
        });
    }

    private void setupChart() {
        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText(getResources().getString(R.string.chart_desc));
        binding.chart.setDescription(description);

        binding.chart.getAxisRight().setEnabled(false);
        binding.chart.getLegend().setEnabled(false);
        binding.chart.setNoDataText(getResources().getString(R.string.no_chart_data));

        List<Point> points = coin.getChart();
        if (points == null) return;
        List<Entry> entries = new ArrayList<>();
        List<Date> x = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            entries.add(new Entry((float) i, (float) point.getPrice()));
            x.add(new Date(point.getTimestamp()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price History");
        int lineColor = getResources().getColor(R.color.chart);
        dataSet.setLineWidth(2f);
        dataSet.setColor(lineColor);
        dataSet.setCircleRadius(3.5f);
        dataSet.setCircleColor(lineColor);
        LineData lineData = new LineData(dataSet);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int i = (int) value;
                return dateFormat.format(x.get(i));
            }
        });
        binding.chart.getAxisLeft().setValueFormatter(new LargeValueFormatter());

        binding.chart.setData(lineData);
        binding.chart.invalidate();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coin = ((MainActivity) getActivity()).coins.get(mIndex);
        setupToolbar();
        setupCoin();
        setupChart();
    }
}