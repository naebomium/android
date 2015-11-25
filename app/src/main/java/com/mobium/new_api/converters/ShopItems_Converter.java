package com.mobium.new_api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mobium.new_api.models.catalog_.ShopItem_;
import com.mobium.new_api.models.catalog_.ShopItems_;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */

public class ShopItems_Converter implements JsonDeserializer<ShopItems_> {
    private static Type itemsType = new TypeToken<ArrayList<ShopItem_>>(){}.getType();
    @Override
    public ShopItems_ deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ShopItems_(
                context.deserialize(json.getAsJsonObject().get("items"), itemsType),
                json.getAsJsonObject().get("totalCount").getAsInt()
        );
    }
}
