package com.mobium.client.models.modifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 *  on 23.09.2015.
 */
public class ModificationImage implements Serializable {

    public String ld;
    public String sd;
    public String hd;
    public String defaultImage;
    public Type type;

    public enum Type implements Serializable {
        MAP("map");
        public final String t;

        static Type find(String t) {
            for (Type type : Type.values())
                if (type.t.equals(t))
                    return type;
            return null;
        }

        Type(String t) {
            this.t = t;
        }
    }

    public ModificationImage(String ld, String sd, String hd, String defaultImage, Type type) {
        this.ld = ld;
        this.sd = sd;
        this.hd = hd;
        this.defaultImage = defaultImage;
        this.type = type;
    }

    public static ModificationImage deserialize(JSONArray images) throws JSONException {
        if (images != null && images.length() > 0) {
            JSONObject firstImage = images.getJSONObject(0);
            JSONObject urlMap = firstImage.getJSONObject("url_map");
            String ld = urlMap.getString("ld");
            String sd = urlMap.getString("sd");
            String hd = urlMap.getString("hd");
            Type type = Type.find(firstImage.getString("type"));
            String defaultImage = firstImage.getString("default");
            return new ModificationImage(ld, sd, hd, defaultImage, type);
        } else {
            return null;
        }
    }
}
