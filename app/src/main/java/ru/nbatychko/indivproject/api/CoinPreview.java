package ru.nbatychko.indivproject.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinPreview {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("api_symbol")
    @Expose
    private String apiSymbol;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("market_cap_rank")
    @Expose
    private Integer marketCapRank;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("large")
    @Expose
    private String large;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiSymbol() {
        return apiSymbol;
    }

    public void setApiSymbol(String apiSymbol) {
        this.apiSymbol = apiSymbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(Integer marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}