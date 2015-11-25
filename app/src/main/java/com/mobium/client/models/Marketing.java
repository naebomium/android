package com.mobium.client.models;

import android.util.Log;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.mobium.client.models.Constants.nullInt;

/**
 *  on 29.04.15.
 * http://wiki.mobium.pro/pages/viewpage.action?pageId=6947552
 * Marketing data implementation 
 */
public abstract class Marketing implements Serializable {
    public final Type type;
    public final String title;
    public final String description;
    public final String iconUrl;

    public Marketing(Type type, String title, String description, String iconUrl) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    /**
     * lowerBy - процент снижения
     * lowerTo - новая цена
     */
    public static class LowerPrice extends Marketing implements Serializable {
        public final int lowerBy; 
        public final int lowerTo;
        
        public LowerPrice(String title, String description,String iconUrl, int lowerBy, int lowerTo) {
            super(Type.LOWER_PRICE, title, description, iconUrl);
            this.lowerBy = lowerBy;
            this.lowerTo = lowerTo;
        }
    }
    public static class FreeDelivery extends Marketing implements Serializable {
        public final String[] deliveryIds;

        public FreeDelivery(String title, String description, String iconUrl,  String[] deliveryIds) {
            super(Type.FREE_DELIVERY, title, description, iconUrl);
            this.deliveryIds = deliveryIds;
        }
    }
    
    public static class Nothing extends Marketing implements Serializable {
        public Nothing( String title, String description, String iconUrl) {
            super(Type.NOTHING, title, description, iconUrl);
        }
    }

    public enum Type implements Serializable {
        LOWER_PRICE("lower_price"), FREE_DELIVERY("free_delivery"), NOTHING("nothing");
        public final String id;

        static Type find(String id) {
            for (Type type : Type.values())
                if (type.id.equals(id))
                    return type;
            return null;
        }

        Type(String id) {
            this.id = id;
        }
    }

    /**
     * Doc: GetItem() method description
     * @param marketing JSONObject marketing item
     * @return deserializable marketing object
     */
    public static Marketing deserialize(JSONObject marketing) throws JSONException {
        String title = marketing.has("title") ? marketing.getString("title") : marketing.getString("Title");
        String typeId = marketing.optString("type");

        String description;

        if (marketing.has("description")) {
            description = marketing.getString("description");
        } else if(marketing.has("Description")) {
            description = marketing.getString("Description");
        } else {
            description = "";
        }

        String urlIcon = marketing.has("icon") ? marketing.getString("icon") : marketing.optString("Icon");

        Type type = Type.find(typeId);
        if (type == null)
            type = Type.NOTHING;


        JSONObject data;
        switch (type) {
            case LOWER_PRICE:
                data = marketing.getJSONObject("data");
                int lowerBy = nullInt;
                int lowerTo = nullInt;
                if (data.has("lower_by"))
                    lowerBy = (int)Double.parseDouble(data.getString("lower_by"));
                if (data.has("lower_to"))
                    lowerTo = (int)Double.parseDouble(data.getString("lower_to"));
                return new LowerPrice(title, description, urlIcon, lowerBy, lowerTo);
            case FREE_DELIVERY:
                data = marketing.getJSONObject("data");
                String[] ids = data.getString("delivery_types").split(",");
                return new FreeDelivery(title, description, urlIcon, ids);
            default:
                return new Nothing(title, description, urlIcon);
        }

    }

    /**
     * Doc: GetItem() method description
     * @param marketings JSONArray of marketing items
     * @return deserializable array of marketin items
     */
    public static Marketing[] deserialize(JSONArray marketings) {
        if (marketings == null || marketings.length() == 0)
            return null;
        List<Marketing> result = new ArrayList<>(marketings.length());

        for (int i = 0; i < marketings.length(); i++) {
            try {
                result.add(deserialize(marketings.getJSONObject(i)));
            } catch (JSONException e) {
                Log.w("marketing", "failed parse marketing object: " + marketings.optJSONObject(i));
                e.printStackTrace();
            }
        }

        return result.size() == 0 ? null : result.toArray(new Marketing[result.size()]);
    }



    public static boolean hasType(Optional<Marketing[]> marketings, Type type) {
        return marketings.isPresent() &&
                Stream.of(marketings.get())
                        .filter(marketing -> marketing.type.equals(type))
                        .findFirst().isPresent();
    }


}
