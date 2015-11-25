package com.mobium.config.block_models;

import android.support.annotation.ColorInt;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;

/**
 *  on 27.10.15.
 */
public class CollectionViewModel extends BlockModel {

    public enum CONTENT_TYPE {
        CATEGORIES, ITEMS
    }

    public enum API_METHOD {
        MARKETING_ITEMS, POPULAR_CATEGORIES
    }

    public enum ContentSource {
        API_METHOD
    }

    public CollectionViewModel() {
        super(BlockType.COLLECTION_VIEW);
    }

    @SerializedName("Colors")
    private Colors colors;

    @SerializedName("ContentType")
    private CONTENT_TYPE contentType;

    @SerializedName("ContentSource")
    private ContentSource contentSource;

    @SerializedName("ApiMethod")
    private API_METHOD apiMethod;

    @SerializedName("SpinnerStyle")
    private String spinnerStyle;

    @SerializedName("Limit")
    private int limit;

    {
        limit = 10;

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Colors getColors() {
        return colors;
    }

    public void setColors(Colors colors) {
        this.colors = colors;
    }

    public CONTENT_TYPE getContentType() {
        return contentType;
    }

    public void setContentType(CONTENT_TYPE contentType) {
        this.contentType = contentType;
    }

    public ContentSource getContentSource() {
        return contentSource;
    }

    public void setContentSource(ContentSource contentSource) {
        this.contentSource = contentSource;
    }

    public API_METHOD getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(API_METHOD apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getSpinnerStyle() {
        return spinnerStyle;
    }

    public void setSpinnerStyle(String spinnerStyle) {
        this.spinnerStyle = spinnerStyle;
    }

    public class Colors {
        @SerializedName("ItemBorderColor")
        private String itemBorderColor;

        @SerializedName("BGColor")
        private String bgColor;

        @SerializedName("ItemBGColor")
        private String itemBgColor;

        @SerializedName("TextColor")
        private String textColor;

        @SerializedName("PriceColor")
        private String priceColor;

        @SerializedName("OldPriceColor")
        private String oldPriceColor;

        public int getOldPriceColor(int defaultColor) {
            return Util.colorFromString(oldPriceColor, defaultColor);
        }

        public void setOldPriceColor(String oldPriceColor) {
            this.oldPriceColor = oldPriceColor;
        }

        public int getItemBorderColor(int defaultColor) {
            return Util.colorFromString(itemBorderColor, defaultColor);
        }

        public void setItemBorderColor(String itemBorderColor) {
            this.itemBorderColor = itemBorderColor;
        }

        public
        @ColorInt
        int getBgColor(@ColorInt int defaultColor) {
            return Util.colorFromString(bgColor, defaultColor);
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public
        @ColorInt
        int getItemBgColor(@ColorInt int defaultColor) {
            return Util.colorFromString(itemBgColor, defaultColor);
        }

        public void setItemBgColor(String itemBgColor) {
            this.itemBgColor = itemBgColor;
        }

        public
        @ColorInt
        int getTextColor(@ColorInt int defaultColor) {
            return Util.colorFromString(textColor, defaultColor);
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public
        @ColorInt
        int getPriceColor(int defaultColor) {
            return Util.colorFromString(priceColor, defaultColor);
        }

        public void setPriceColor(String priceColor) {
            this.priceColor = priceColor;
        }

    }

}
