package ru.nbatychko.indivproject.db;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import ru.nbatychko.indivproject.api.CoinDetail;

public class CoinModel {
    private Long id;
    private String apiId, name, symbol;
    private double price, priceChange, marketCap;
    private String image;
    private List<Point> chart;

    public CoinModel() {
    }

    public CoinModel(CoinDetail coinDetail) {
        apiId = coinDetail.getId();
        name = coinDetail.getName();
        setSymbol(coinDetail.getSymbol());
        price = coinDetail.getCurrentPrice();
        priceChange = coinDetail.getPriceChangePercentage24h();
        marketCap = coinDetail.getMarketCap();
        image = coinDetail.getImage();
    }

    private String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("USD"));
        if (amount < 10) {
            format.setMaximumFractionDigits(4);
        }
        return format.format(amount);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol.toUpperCase(Locale.ENGLISH);
    }

    public double getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return formatCurrency(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public String getFormattedPriceChange() {
        String prefix = "";
        if (priceChange > 0) {
            prefix = "▲";
        } else if (priceChange < 0) {
            prefix = "▼";
        }
        return prefix + String.format(Locale.getDefault(), "%1$,.2f%%", priceChange);
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public String getFormattedMarketCap() {
        return formatCurrency(marketCap);
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Point> getChart() {
        return chart;
    }

    public void setChart(List<Point> chart) {
        this.chart = chart;
    }
}
