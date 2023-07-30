package com.cryptowatch.app.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cryptowatch.app.api.CryptoCompareService;
import com.cryptowatch.app.models.Currency;
import com.cryptowatch.app.models.Value;
import com.cryptowatch.app.utilities.Constants;
import com.cryptowatch.app.utilities.CurrencyDeserializer;
import com.cryptowatch.app.utilities.ValueDeserializer;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioRepository {
    private static PortfolioRepository instance;

    private final Database database;
    private final MutableLiveData<List<Currency>> portfolio;

    private PortfolioRepository(Database database) {
        this.database = database;
        this.portfolio = new MutableLiveData<>(new ArrayList<>());
    }

    public static PortfolioRepository getInstance(Database database) {
        if (instance == null) {
            instance = new PortfolioRepository(database);
        }
        return instance;
    }

    public MutableLiveData<List<Currency>> getCurrenciesFromPortfolio() {
        List<String> currenciesId = getAllCurrencyId();
        if (currenciesId.size() == portfolio.getValue().size()) {
            return portfolio;
        }

        CryptoCompareService currencyService = CryptoCompareService.Client.getService(
                new TypeToken<List<Currency>>() {}.getType(), new CurrencyDeserializer());
        CryptoCompareService valueService = CryptoCompareService.Client.getService(Value.class, new ValueDeserializer());

        for (String id : currenciesId) {
            getCurrencyById(id, currencyService, valueService);
        }

        return portfolio;
    }

    private void getCurrencyById(String id, CryptoCompareService currencyService, CryptoCompareService valueService) {
        Call<List<Currency>> call = currencyService.getCurrencySummary(id);

        call.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                Currency currency = response.body().get(0);
                currency.setInPortfolio(true);
                getCurrencyValue(currency, valueService);
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                Log.d("getCurrencySummary", t.getMessage());
            }
        });
    }

    private void getCurrencyValue(Currency currency, CryptoCompareService valueService) {
        Call<Value> call = valueService.getCurrencyValue(currency.getId(), Constants.CONVERSION_CURRENCY);

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                currency.setValue(response.body());
                portfolio.getValue().add(currency);
                portfolio.setValue(portfolio.getValue());
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.d("getCurrencyValue", t.getMessage());
            }
        });
    }

    public void insertCurrency(Currency currency) {
        if (currency.isInPortfolio()) {
            return;
        }
        currency.setInPortfolio(true);
        portfolio.getValue().add(currency);
        portfolio.setValue(portfolio.getValue());

        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Currency.FIELD_ID, currency.getId());
        db.insert(Currency.TABLE_NAME, null, cv);
    }

    public int deleteCurrency(Currency currency) {
        currency.setInPortfolio(false);
        portfolio.getValue().remove(currency);
        portfolio.setValue(portfolio.getValue());

        SQLiteDatabase db = database.getReadableDatabase();
        return db.delete(Currency.TABLE_NAME, Currency.FIELD_ID + "=?", new String[] {currency.getId()});
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllCurrencyId() {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = String.format("SELECT * FROM %s", Currency.TABLE_NAME);
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(Currency.FIELD_ID)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}