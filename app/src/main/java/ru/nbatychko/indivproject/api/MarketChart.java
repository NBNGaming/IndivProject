package ru.nbatychko.indivproject.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketChart {
    @SerializedName("prices")
    @Expose
    private List<String[]> prices;
    @SerializedName("market_caps")
    @Expose
    private List<String[]> marketCaps;

    public List<String[]> getPrices() {
        return prices;
    }

    public void setPrices(List<String[]> prices) {
        this.prices = prices;
    }

    public List<String[]> getMarketCaps() {
        return marketCaps;
    }

    public void setMarketCaps(List<String[]> marketCaps) {
        this.marketCaps = marketCaps;
    }
}