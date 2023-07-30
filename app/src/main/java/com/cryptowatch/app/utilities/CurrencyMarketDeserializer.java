package com.cryptowatch.app.utilities;

import com.cryptowatch.app.models.Currency;
import com.cryptowatch.app.models.Value;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CurrencyMarketDeserializer implements JsonDeserializer<List<Currency>> {
    @Override
    public List<Currency> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Currency> currencyList = new ArrayList<>();

        final JsonArray currencyArray = json.getAsJsonObject().getAsJsonArray("Data");

        for (JsonElement el : currencyArray) {
            final JsonObject currency = el.getAsJsonObject();
            if (currency.getAsJsonObject("RAW") == null || currency.getAsJsonObject("DISPLAY") == null) {
                continue;
            }

            final JsonObject coinInfo = currency.getAsJsonObject("CoinInfo");
            final JsonObject rawValue = currency.getAsJsonObject("RAW").getAsJsonObject(Constants.CONVERSION_CURRENCY);
            final JsonObject value = currency.getAsJsonObject("DISPLAY").getAsJsonObject(Constants.CONVERSION_CURRENCY);

            currencyList.add(new Currency(
                    coinInfo.get("Name").getAsString(),
                    coinInfo.get("FullName").getAsString(),
                    (coinInfo.get("ImageUrl") != null
                            ? coinInfo.get("ImageUrl").getAsString()
                            : null),
                    new Value(
                            rawValue.get("PRICE").getAsDouble(),
                            value.get("PRICE").getAsString(),
                            value.get("MKTCAP").getAsString(),
                            value.get("SUPPLY").getAsString(),
                            value.get("CHANGEPCTHOUR").getAsString() + "%",
                            value.get("CHANGEPCT24HOUR").getAsString() + "%",
                            value.get("VOLUME24HOUR").getAsString(),
                            value.get("HIGH24HOUR").getAsString(),
                            value.get("LOW24HOUR").getAsString()
                    ),
                    false
            ));
        }

        return currencyList;
    }
}