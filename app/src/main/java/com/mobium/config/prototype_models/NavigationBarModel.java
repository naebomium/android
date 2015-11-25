package com.mobium.config.prototype_models;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;
import com.mobium.config.prototype.INavigationBar;

/**
 *  on 16.11.15.
 *
 "navigation_bar": {
     "color_background": "#11223344",
     "color_text":"colors_text_color",

     "ui_cart_enabled":false,
     "ui_gravity":"center",
     "ui_text":"checkout_screen_title",
     "ui_mode":"icon_text",

     "ui_home_icon":"native_icon",
     "ui_home_native_icon_color":"#11223344"
 }

 */
public class NavigationBarModel {

    @SerializedName("color_background")
    private String colorBackground;

    @SerializedName("color_text")
    private String colorText;

    @SerializedName("ui_cart_enabled")
    private boolean uiCartEnabled;

    @SerializedName("ui_gravity")
    private String uiGravity;

    @SerializedName("ui_text")
    private String uiText;


    @SerializedName("ui_mode")
    private String uiMode;

    @SerializedName("ui_home_icon")
    private String uiHomeIcon;

    @SerializedName("ui_home_native_icon_color")
    private String uiHomeNativeIconColor;

    @SerializedName("ui_home_custom_icon_res")
    private String uiHomeCustomIconRes;

    @SerializedName("color_menu_icon")
    private String colorMenuIcon;

    /**
     *
     * @return
     * The getColorBackground
     */
    public String getColorBackground() {
        return colorBackground;
    }

    /**
     *
     * @param colorBackground
     * The color_background
     */
    public void setColorBackground(String colorBackground) {
        this.colorBackground = colorBackground;
    }

    /**
     *
     * @return
     * The getNativeMenuColor
     */
    public String getColorMenuIcon() {
        return colorMenuIcon;
    }

    /**
     *
     * @param colorMenuIcon
     * The color_menu_icon
     */
    public void setColorMenuIcon(String colorMenuIcon) {
        this.colorMenuIcon = colorMenuIcon;
    }

    /**
     *
     * @return
     * The getColorOfText
     */
    public String getColorText() {
        return colorText;
    }

    /**
     *
     * @param colorText
     * The color_text
     */
    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    /**
     *
     * @return
     * The uiCartEnabled
     */
    public boolean isUiCartEnabled() {
        return uiCartEnabled;
    }

    /**
     *
     * @param uiCartEnabled
     * The ui_cart_enabled
     */
    public void setUiCartEnabled(boolean uiCartEnabled) {
        this.uiCartEnabled = uiCartEnabled;
    }

    /**
     *
     * @return
     * The getGravity
     */
    public String  getUiGravity() {
        return uiGravity;
    }

    /**
     *
     * @param uiGravity
     * The ui_gravity
     */
    public void setUiGravity(String uiGravity) {
        this.uiGravity = uiGravity;
    }

    /**
     *
     * @return
     * The getText
     */
    public String getUiText() {
        return uiText;
    }

    /**
     *
     * @param uiText
     * The ui_text
     */
    public void setUiText(String uiText) {
        this.uiText = uiText;
    }



    /**
     *
     * @return
     * The getMode
     */
    public String getUiMode() {
        return uiMode;
    }

    /**
     *
     * @param uiMode
     * The ui_mode
     */
    public void setUiMode(String uiMode) {
        this.uiMode = uiMode;
    }

    public String getUiHomeIcon() {
        return uiHomeIcon;
    }

    public void setUiHomeIcon(String uiHomeIcon) {
        this.uiHomeIcon = uiHomeIcon;
    }

    public String getUiHomeNativeIconColor() {
        return uiHomeNativeIconColor;
    }

    public void setUiHomeNativeIconColor(String uiHomeNativeIconColor) {
        this.uiHomeNativeIconColor = uiHomeNativeIconColor;
    }

    public String getUiHomeCustomIconRes() {
        return uiHomeCustomIconRes;
    }

    public void setUiHomeCustomIconRes(String uiHomeCustomIconRes) {
        this.uiHomeCustomIconRes = uiHomeCustomIconRes;
    }
}
