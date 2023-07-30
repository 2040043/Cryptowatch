package com.cryptowatch.app.utilities;

import com.cryptowatch.app.models.Currency;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyDeserializer implements JsonDeserializer<List<Currency>> {
    @Override
    public List<Currency> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<JsonElement> array = json.getAsJsonObject()
                .getAsJsonObject("Data")
                .entrySet()
                .stream()
                .map(i -> i.getValue())
                .collect(Collectors.toList());

        ArrayList<Currency> currencies = new ArrayList<>();

        for (JsonElement el : array) {
            JsonObject currency = el.getAsJsonObject();

            String fullName = currency.get("FullName").getAsString();

            currencies.add(new Currency(
                    currency.get("Symbol").getAsString(),
                    fullName.substring(0, fullName.indexOf("(") - 1),
                    (currency.get("ImageUrl") != null
                            ? currency.get("ImageUrl").getAsString()
                            : null),
                    null,
                    false
            ));
        }

        return currencies;
    }
}