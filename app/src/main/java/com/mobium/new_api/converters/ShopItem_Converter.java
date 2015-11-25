package com.mobium.new_api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mobium.client.models.Marketing;
import com.mobium.new_api.models.Image;
import com.mobium.new_api.models.ShopItemExtra;
import com.mobium.new_api.models.catalog_.Characteristics_;
import com.mobium.new_api.models.catalog_.Filter_;
import com.mobium.new_api.models.catalog_.Media_;
import com.mobium.new_api.models.catalog_.ShopItem_;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public class ShopItem_Converter implements JsonDeserializer<ShopItem_> {

    private static final Type filteringType = new TypeToken<ArrayList<Filter_>>(){}.getType();
    private static final Type characteristicsType = new TypeToken<ArrayList<Characteristics_>>(){}.getType();
    private static final Type marketingsType = new TypeToken<ArrayList<Marketing>>(){}.getType();
    private static final Type mediaType = new TypeToken<ArrayList<Media_>>(){}.getType();
    private static final Type sortingsType = new TypeToken<Hashtable<String, String>>(){}.getType();


    @Override
    public ShopItem_ deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();
        ShopItem_ result = new ShopItem_(
                root.get("id").getAsString(),
                root.get("title").getAsString(),
                root.get("cost").getAsInt()
        );
        try {

        if (root.has("description"))
            result.setDescription(root.get("description").getAsString());

        if (root.has("icon"))
            result.setIcon(context.deserialize(root.get("icon"), Image.class));

        if (root.has("images"))
            result.setIcon(context.deserialize(root.get("images"), Image.class));

        if (root.has("media"))
            result.setMedia(context.deserialize(root.get("media"), mediaType));

        if (root.has("marketing"))
            if (root.get("marketing").isJsonArray())
                if (root.get("marketing").getAsJsonArray().size() > 0)
                    result.setMarketing(context.deserialize(root.get("marketing"), marketingsType));

        if (root.has("characteristics"))
            result.setCharacteristics(context.deserialize(root.get("characteristics"), characteristicsType));

        if (root.has("extra"))
            result.setExtra(context.deserialize(root.get("extra"), ShopItemExtra.class));

        if (root.has("sortings"))
            if (root.get("sortings").isJsonObject())
                if (root.getAsJsonObject("sortings").entrySet().size() > 0)
                    result.setSortings(context.deserialize(root.get("sortings"), sortingsType));

        if (root.has("filterings"))
            if (root.get("filterings").isJsonArray())
                if (root.getAsJsonArray("filterings").size() > 0)
                    result.setFilterings(context.deserialize(root.get("filterings"), filteringType));

        if (root.has("sortingOrder"))
            result.setSortingOrder(root.get("sortingOrder").getAsInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
