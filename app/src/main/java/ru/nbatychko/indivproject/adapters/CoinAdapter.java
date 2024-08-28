package ru.nbatychko.indivproject.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.nbatychko.indivproject.R;
import ru.nbatychko.indivproject.db.CoinModel;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.ViewHolder> {
    public interface OnCoinClickListener {
        void onCoinClick(int position);
    }

    private final OnCoinClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<CoinModel> coins;

    public CoinAdapter(Context context, List<CoinModel> coins, OnCoinClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.coins = coins;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CoinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CoinAdapter.ViewHolder holder, int position) {
        CoinModel coin = coins.get(position);
        Picasso.get().load(coin.getImage()).into(holder.logoView);
        holder.symbolView.setText(coin.getSymbol());
        holder.nameView.setText(coin.getName());
        holder.priceView.setText(coin.getFormattedPrice());

        holder.priceChangeView.setText(coin.getFormattedPriceChange());
        if (coin.getPriceChange() > 0) {
            holder.priceChangeView.setTextColor(holder.upColor);
        } else if (coin.getPriceChange() < 0) {
            holder.priceChangeView.setTextColor(holder.downColor);
        }

        holder.itemView.setOnClickListener(v -> onClickListener.onCoinClick(position));
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView logoView;
        final TextView symbolView, nameView, priceView, priceChangeView;
        final int upColor, downColor;

        public ViewHolder(View itemView) {
            super(itemView);
            logoView = itemView.findViewById(R.id.logo);
            symbolView = itemView.findViewById(R.id.symbol);
            nameView = itemView.findViewById(R.id.name);
            priceView = itemView.findViewById(R.id.price);
            priceChangeView = itemView.findViewById(R.id.priceChange);

            Resources res = itemView.getContext().getResources();
            upColor = res.getColor(R.color.change_up);
            downColor = res.getColor(R.color.change_down);
        }
    }
}
