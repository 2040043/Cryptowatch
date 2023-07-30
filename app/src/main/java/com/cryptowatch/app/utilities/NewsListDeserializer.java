package com.cryptowatch.app.utilities;

import com.cryptowatch.app.models.NewsArticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsListDeserializer implements JsonDeserializer<List<NewsArticle>> {
    @Override
    public List<NewsArticle> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<NewsArticle> articleList = new ArrayList<>();

        final JsonArray articleArray = json.getAsJsonObject().getAsJsonArray("Data");

        for (JsonElement el : articleArray) {
            final JsonObject article = el.getAsJsonObject();
            final JsonObject source = article.getAsJsonObject("source_info");

            articleList.add(new NewsArticle(
                    article.get("title").getAsString(),
                    article.get("imageurl").getAsString(),
                    source.get("name").getAsString(),
                    article.get("url").getAsString()
            ));
        }

        return articleList;
    }
}
