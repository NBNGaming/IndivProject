package ru.nbatychko.indivproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ru.nbatychko.indivproject.MainActivity;
import ru.nbatychko.indivproject.R;
import ru.nbatychko.indivproject.adapters.CoinAdapter;
import ru.nbatychko.indivproject.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private CoinAdapter coinAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.inflateMenu(R.menu.menu_main);
        MainActivity main = (MainActivity) getActivity();

        coinAdapter = new CoinAdapter(getContext(), main.coins, position -> {
            DetailFragment fragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            fragment.setArguments(bundle);
            main.switchFragment(fragment);
        });
        binding.list.setAdapter(coinAdapter);
        main.coinAdapter = coinAdapter;

        binding.toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_add) {
                main.switchFragment(new AddFragment());
                return true;
            } else if (itemId == R.id.action_update) {
                main.updateAllCoins();
                return true;
            }
            return false;
        });
    }
}