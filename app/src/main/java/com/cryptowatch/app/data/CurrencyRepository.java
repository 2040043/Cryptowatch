package com.cryptowatch.app.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cryptowatch.app.api.CryptoCompareService;
import com.cryptowatch.app.models.Currency;
import com.cryptowatch.app.models.Ohlc;
import com.cryptowatch.app.models.Value;
import com.cryptowatch.app.utilities.Constants;
import com.cryptowatch.app.utilities.OhlcListDeserializer;
import com.cryptowatch.app.utilities.ValueDeserializer;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyRepository {
    private MutableLiveData<Currency> currency;
    private MutableLiveData<List<Ohlc>> ohlc;

    public CurrencyRepository(Currency curr) {
        currency = new MutableLiveData<>(curr);
        ohlc = new MutableLiveData<>();
    }

    public MutableLiveData<Currency> getCurrencyValue() {
        CryptoCompareService valueService = CryptoCompareService.Client.getService(Value.class, new ValueDeserializer());

        Currency curr = currency.getValue();
        if (curr.getValue() != null) {
            return currency;
        }

        Call<Value> call = valueService.getCurrencyValue(curr.getId(), Constants.CONVERSION_CURRENCY);

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                curr.setValue(response.body());
                currency.setValue(curr);
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.d("getCurrencyValue", t.getMessage());
            }
        });

        return currency;
    }

    public MutableLiveData<List<Ohlc>> getOhlc(String currencyId, String type, int count) {
        CryptoCompareService service = CryptoCompareService.Client.getService(
                new TypeToken<List<Ohlc>>() {}.getType(), new OhlcListDeserializer());

        Call<List<Ohlc>> call;

        switch (type) {
            case "minute":
                call = service.getOhlcMinute(currencyId, Constants.CONVERSION_CURRENCY, count);
                break;
            case "hourly":
                call = service.getOhlcHourly(currencyId, Constants.CONVERSION_CURRENCY, count);
                break;
            case "daily":
                call = service.getOhlcDaily(currencyId, Constants.CONVERSION_CURRENCY, count);
                break;
            default:
                return ohlc;
        }

        call.enqueue(new Callback<List<Ohlc>>() {
            @Override
            public void onResponse(Call<List<Ohlc>> call, Response<List<Ohlc>> response) {
                ohlc.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Ohlc>> call, Throwable t) {
                Log.d("getOhlc", t.getMessage());
            }
        });

        return ohlc;
    }
}
