package com.mobium.config.prototype_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *  on 16.11.15.
 */
public class Cell {

    @SerializedName("action_type")
    @Expose
    private String actionType;
    @SerializedName("action_data")
    @Expose
    private String actionData;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;

    /**
     *
     * @return
     * The actionType
     */
    public String getActionType() {
        return actionType;
    }

    /**
     *
     * @param actionType
     * The action_type
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     *
     * @return
     * The actionData
     */
    public String getActionData() {
        return actionData;
    }

    /**
     *
     * @param actionData
     * The action_data
     */
    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(String image) {
        this.image = image;
    }

}
