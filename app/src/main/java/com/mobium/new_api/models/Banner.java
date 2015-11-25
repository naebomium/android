package com.mobium.new_api.models;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.io.Serializable;

/**
 *  on 18.06.15.
 * http://mobiumapps.com/
 */

public class Banner implements Serializable {
    @SerializedName("targetData")
    private String actionData;
    @SerializedName("targetType")
    private String actionType;

    public String id;
    public Image image;

    public Banner(String id, String actionType, String actionData, Image image) {
        this.id = id;
        this.actionType = actionType;
        this.actionData = actionData;
        this.image = image;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public static Banner[] testData() {
        return new Banner[] {
                new Banner("1", "doOpenCatalog", "", Image.testImage()),
                new Banner("2", "doOpenCatalog", "", Image.testImage()),
                new Banner("3", "doOpenCatalog", "", Image.testImage()),
                new Banner("4", "doOpenCatalog", "", Image.testImage()),
        };
    }

    public static Banner[] getTestBaners(int size) {
        Banner[] banners = new Banner[size];
        for (int i = 0; i < size; i++) {
            banners[i] = new Banner("" +i,"doOpenCatalog", ""+i ,Image.testImage() );
        }
        return banners;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Banner() {
    }
}
