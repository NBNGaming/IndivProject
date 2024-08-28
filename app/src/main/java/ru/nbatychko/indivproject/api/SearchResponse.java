package ru.nbatychko.indivproject.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @SerializedName("coins")
    @Expose
    private List<CoinPreview> coins;

    public List<CoinPreview> getCoins() {
        return coins;
    }

    public void setCoins(List<CoinPreview> coins) {
        this.coins = coins;
    }
}