package ru.nbatychko.indivproject.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nbatychko.indivproject.BuildConfig;

public interface CoinGeckoApi {
    @GET("coins/markets?sparkline=false&price_change_percentage=24h&locale=ru&x_cg_demo_api_key=" + BuildConfig.API_KEY)
    Call<List<CoinDetail>> getMarketDataForCoins(
            @Query("ids") String ids,
            @Query("vs_currency") String vsCurrency
    );

    @GET("search?x_cg_demo_api_key=" + BuildConfig.API_KEY)
    Call<SearchResponse> searchCoins(
            @Query("query") String query
    );

    @GET("coins/{id}/market_chart?x_cg_demo_api_key=" + BuildConfig.API_KEY)
    Call<MarketChart> getMarketChart(
            @Path("id") String id,
            @Query("vs_currency") String vsCurrency,
            @Query("days") String days,
            @Query("interval") String interval
    );
}