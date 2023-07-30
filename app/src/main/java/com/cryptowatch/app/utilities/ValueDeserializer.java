package com.cryptowatch.app.utilities;

import com.cryptowatch.app.models.Value;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class ValueDeserializer implements JsonDeserializer<Value> {
    @Override
    public Value deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rawData = json.getAsJsonObject().getAsJsonObject("RAW");
        JsonObject data = json.getAsJsonObject().getAsJsonObject("DISPLAY");

        JsonObject rawValue = getCurrencyObject(rawData, Constants.CONVERSION_CURRENCY);
        JsonObject value = getCurrencyObject(data, Constants.CONVERSION_CURRENCY);

        return new Value(
                rawValue.get("PRICE").getAsDouble(),
                value.get("PRICE").getAsString(),
                value.get("MKTCAP").getAsString(),
                value.get("SUPPLY").getAsString(),
                value.get("CHANGEPCTHOUR").getAsString() + "%",
                value.get("CHANGEPCT24HOUR").getAsString() + "%",
                value.get("VOLUME24HOUR").getAsString(),
                value.get("HIGH24HOUR").getAsString(),
                value.get("LOW24HOUR").getAsString()
        );
    }

    private JsonObject getCurrencyObject(JsonObject data, String currency) {
        return data.entrySet()
                .stream()
                .map(i -> i.getValue())
                .collect(Collectors.toList())
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject(currency);
    }
}