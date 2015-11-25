package com.mobium.config.prototype_models;

import com.google.gson.annotations.SerializedName;

/**
 *  on 16.11.15.
 */
public class DesignModel {

    @SerializedName("navigation_bar")
    private NavigationBarModel model;

    @SerializedName("left_menu")
    private LeftMenuModel leftMenuModel;

    public NavigationBarModel getModel() {
        return model;
    }

    public void setModel(NavigationBarModel model) {
        this.model = model;
    }

    public LeftMenuModel getLeftMenuModel() {
        return leftMenuModel;
    }

    public void setLeftMenuModel(LeftMenuModel leftMenuModel) {
        this.leftMenuModel = leftMenuModel;
    }
}
