package com.mobium.client.models.modifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  on 17.09.2015.
 */
public class OfferModification implements Serializable {
    public final String id;
    public final String title;
    public final ModificationImage images;
    //!todo add marketing
    public final int price;
    public final String article;
    public final Group group;

    public OfferModification(String id, String title, ModificationImage images, int price, String article, Group group) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.article = article;
        this.images = images;
        this.group = group;
    }

    public static OfferModification deserialize (JSONObject modification) throws JSONException {
        String id  = modification.getString("id");
        String title = modification.getString("title");
        int price = modification.getInt("price");
        String article = modification.getString("article");
        Group group = Group.deserialize(modification.getJSONObject("params"));
        ModificationImage images = ModificationImage.deserialize(modification.getJSONArray("images"));
        return new OfferModification(id, title, images, price, article, group);
    }

    public static OfferModification[] deserialize (JSONArray jsonArray) throws JSONException {
        int size = jsonArray.length();
        List<OfferModification> result = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            try {
                result.add(deserialize(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result.size() == 0 ? null : result.toArray(new OfferModification[result.size()]);
    }

}
