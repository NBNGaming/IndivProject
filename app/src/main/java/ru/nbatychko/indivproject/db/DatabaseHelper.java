package ru.nbatychko.indivproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "crypto.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "coins";

    public static final String KEY_ID = "_id";
    public static final String KEY_API_ID = "api_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SYMBOL = "symbol";
    public static final String KEY_PRICE = "price";
    public static final String KEY_PRICE_CHANGE = "price_change";
    public static final String KEY_MARKET_CAP = "market_cap";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CHART = "chart";

    private static DatabaseHelper mInstance;
    private final Gson gson;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        gson = new Gson();
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_API_ID + " TEXT UNIQUE," +
                KEY_NAME + " TEXT," +
                KEY_SYMBOL + " TEXT," +
                KEY_PRICE + " REAL," +
                KEY_PRICE_CHANGE + " REAL," +
                KEY_MARKET_CAP + " REAL," +
                KEY_IMAGE + " TEXT," +
                KEY_CHART + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void addCoin(CoinModel coin) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_API_ID, coin.getApiId());
        values.put(KEY_NAME, coin.getName());
        values.put(KEY_SYMBOL, coin.getSymbol());
        values.put(KEY_PRICE, coin.getPrice());
        values.put(KEY_PRICE_CHANGE, coin.getPriceChange());
        values.put(KEY_MARKET_CAP, coin.getMarketCap());
        values.put(KEY_IMAGE, coin.getImage());
        values.put(KEY_CHART, serializeChart(coin.getChart()));

        long result = db.insert(TABLE, null, values);
        if (result > 0) {
            coin.setId(result);
        }
    }

    public void updateCoin(CoinModel coin) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, coin.getName());
        values.put(KEY_SYMBOL, coin.getSymbol());
        values.put(KEY_PRICE, coin.getPrice());
        values.put(KEY_PRICE_CHANGE, coin.getPriceChange());
        values.put(KEY_MARKET_CAP, coin.getMarketCap());
        values.put(KEY_IMAGE, coin.getImage());
        values.put(KEY_CHART, serializeChart(coin.getChart()));

        db.update(TABLE, values, KEY_API_ID + " = ?", new String[]{coin.getApiId()});
    }

    public void deleteCoin(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, KEY_ID + " = " + id, null);
    }

    public List<CoinModel> getAllCoins() {
        SQLiteDatabase db = getReadableDatabase();
        List<CoinModel> coins = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                CoinModel newCoin = new CoinModel();

                newCoin.setId(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
                newCoin.setApiId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_API_ID)));
                newCoin.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
                newCoin.setSymbol(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SYMBOL)));
                newCoin.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                newCoin.setPriceChange(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE_CHANGE)));
                newCoin.setMarketCap(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_MARKET_CAP)));
                newCoin.setImage(cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)));

                String chartJson = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHART));
                newCoin.setChart(deserializeChart(chartJson));

                coins.add(newCoin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return coins;
    }

    private String serializeChart(List<Point> chart) {
        return gson.toJson(chart);
    }

    private List<Point> deserializeChart(String json) {
        Type listType = new TypeToken<ArrayList<Point>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
}