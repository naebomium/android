package com.mobium.new_api.models;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.io.Serializable;

/**
 *  on 18.06.15.
 * http://mobiumapps.com/
 * http://wiki.mobium.pro/pages/viewpage.action?pageId=6947565
 */


public class Image implements Serializable {
    @SerializedName("default")
    public final String defaultUrl;
    public final Type type;

    @SerializedName("url_map")
    private ImageMap imageMap;

    public Image(String defaultUrl, Type type) {
        this.defaultUrl = defaultUrl;
        this.type = type;
    }

    enum Type {
        map, raw_url, @SerializedName("null") Null
    }


    public ImageMap getImageMap() {
        return imageMap;
    }

    public void setImageMap(ImageMap imageMap) {
        this.imageMap = imageMap;
    }

    public String getHd() {
        return imageMap != null ? (imageMap.hd != null ?  imageMap.hd : defaultUrl) : defaultUrl;
    }
    public String getSd() {
        return imageMap != null ? (imageMap.sd != null ?  imageMap.sd : defaultUrl) : defaultUrl;
    }

    public static Image testImage() {
        return new Image("http://cs411421.vk.me/v411421599/8b13/ZsUeB4Oe-jU.jpg", Type.raw_url);
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public Type getType() {
        return type;
    }
}
