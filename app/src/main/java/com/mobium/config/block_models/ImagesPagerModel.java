package com.mobium.config.block_models;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;
import com.mobium.config.block_views.ImagesPager;

/**
 *  on 29.10.2015.
 */
public class ImagesPagerModel extends BlockModel {

    public ImagesPagerModel() {
        super(BlockType.IMAGES_PAGER);
    }

    public enum CONTENT_SOURCE {
        API_METHOD
    }

    public enum API_METHOD_TYPE {
        BANNERS
    }

    public enum SPINNER_STYLE {
        @SerializedName("White")
        WHITE;

        public int getColor() {
            int color = 0;
            switch (name().toLowerCase()) {
                case "white":
                    color = Color.WHITE;
                    break;
            }
            return color;
        }
    }

    @SerializedName("SelectedPageIndicatorColor")
    private String selectedPageIndicatorColor;

    public int getSelectedPageIndicatorColor(@ColorInt int defaultColor) {
        return Util.colorFromString(selectedPageIndicatorColor, defaultColor);
    }

    public void setSelectedPageIndicatorColor(String selectedPageIndicatorColor) {
        this.selectedPageIndicatorColor = selectedPageIndicatorColor;
    }

    public @ColorInt int getPageIndicatorColor(@ColorInt int defaultColor) {
        return Util.colorFromString(pageIndicatorColor, defaultColor);
    }

    public void setPageIndicatorColor(String pageIndicatorColor) {
        this.pageIndicatorColor = pageIndicatorColor;
    }

    public SPINNER_STYLE getSpinnerStyle() {
        return spinnerStyle;
    }

    public void setSpinnerStyle(SPINNER_STYLE spinnerStyle) {
        this.spinnerStyle = spinnerStyle;
    }

    public double getBannerHeightRatio() {
        return bannerHeightRatio;
    }

    public void setBannerHeightRatio(double bannerHeightRatio) {
        this.bannerHeightRatio = bannerHeightRatio;
    }

    public API_METHOD_TYPE getApiMethodType() {
        return apiMethodType;
    }

    public void setApiMethodType(API_METHOD_TYPE apiMethodType) {
        this.apiMethodType = apiMethodType;
    }

    public CONTENT_SOURCE getContentSource() {
        return contentSource;
    }

    public void setContentSource(CONTENT_SOURCE contentSource) {
        this.contentSource = contentSource;
    }

    @SerializedName("PageIndicatorColor")
    private String pageIndicatorColor;
    @SerializedName("SpinnerStyle")
    private SPINNER_STYLE spinnerStyle;
    @SerializedName("HeightRatio")
    private double bannerHeightRatio;
    @SerializedName("ImagesPagerApiMethodType")
    private API_METHOD_TYPE apiMethodType;
    @SerializedName("ImagesPagerContentSource")
    private CONTENT_SOURCE contentSource;

}
