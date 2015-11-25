package com.mobium.client.models.modifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 *  on 23.09.2015.
 */
public class Group implements Serializable {
    public final String title;
    public final String keyTitle;
    public final String valueTitle;

    public Group (String title, String keyTitle, String valueTitle){
        this.title = title;
        this.keyTitle = keyTitle;
        this.valueTitle = valueTitle;
    }

    public static Group deserialize(JSONObject params) throws JSONException {
        JSONArray groups = params.getJSONArray("groups");
        JSONObject firstGroup = groups.getJSONObject(0);
        String title = firstGroup.getString("title");
        JSONArray items = firstGroup.getJSONArray("items");
        JSONObject firstItem = items.getJSONObject(0);
        String keyTitle = firstItem.getString("keyTitle");
        String valueTitle = firstItem.getString("valueTitle");
        return new Group(title, keyTitle, valueTitle);
    }
}
