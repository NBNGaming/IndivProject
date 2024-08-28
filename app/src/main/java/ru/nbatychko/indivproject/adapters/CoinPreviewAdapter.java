package ru.nbatychko.indivproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.nbatychko.indivproject.R;
import ru.nbatychko.indivproject.api.CoinPreview;

public class CoinPreviewAdapter extends RecyclerView.Adapter<CoinPreviewAdapter.ViewHolder> {
    public interface OnPreviewClickListener {
        void onPreviewClick(CoinPreview coin);
    }

    private final OnPreviewClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<CoinPreview> coins;

    public CoinPreviewAdapter(Context context, List<CoinPreview> coins, OnPreviewClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.coins = coins;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CoinPreviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CoinPreviewAdapter.ViewHolder holder, int position) {
        CoinPreview coin = coins.get(position);
        Picasso.get().load(coin.getLarge()).into(holder.logoView);
        holder.symbolView.setText(coin.getSymbol());
        holder.nameView.setText(coin.getName());

        holder.itemView.setOnClickListener(v -> onClickListener.onPreviewClick(coin));
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView logoView;
        final TextView symbolView, nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            logoView = itemView.findViewById(R.id.logoS);
            symbolView = itemView.findViewById(R.id.symbolS);
            nameView = itemView.findViewById(R.id.nameS);
        }
    }
}
