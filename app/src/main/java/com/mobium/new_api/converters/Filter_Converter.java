package com.mobium.new_api.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mobium.client.models.filters.Filtering;
import com.mobium.new_api.models.catalog_.FilterFlags_;
import com.mobium.new_api.models.catalog_.FilterRange_;
import com.mobium.new_api.models.catalog_.FilterSingle_;
import com.mobium.new_api.models.catalog_.Filter_;

import java.lang.reflect.Type;
import java.util.HashSet;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public class Filter_Converter implements JsonDeserializer<Filter_> {
    @Override
    public Filter_ deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String id = json.getAsJsonObject().get("id").getAsString();
        switch (Filtering.Type.valueOf(json.getAsJsonObject().get("type").getAsString())) {
            case single:
                return new FilterSingle_(Filtering.Type.single, id, json.getAsJsonObject().get("value").getAsBoolean());
            case range:
                return new FilterRange_(Filtering.Type.range, id, json.getAsJsonObject().get("value").getAsDouble());
            case flags:
                JsonArray values = json.getAsJsonObject().get("values").getAsJsonArray();
                HashSet<String> set = new HashSet<>(values.size());
                for (JsonElement element : values)
                    set.add(element.getAsString());
                return new FilterFlags_(Filtering.Type.flags, id, set);
            default:
                return null;
        }
    }
}
