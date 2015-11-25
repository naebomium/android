package com.mobium.config.block_models;

import android.support.annotation.ColorInt;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;

/**
 *  on 11/9/15.
 * MobiumApps http://mobiumapps.com/
 */
public class ToCartModel {

    @SerializedName("Colors")
    private Colors colors;

    @SerializedName("Colors")
    private Images images;

    @SerializedName("Borders")
    private Borders borders;

    public Colors getColors() {
        return colors;
    }

    public void setColors(Colors colors) {
        this.colors = colors;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Borders getBorders() {
        return borders;
    }

    public void setBorders(Borders borders) {
        this.borders = borders;
    }

    public class Images {
        @SerializedName("ToCart")
        private String toCart;

        @SerializedName("CartLoaded")
        private String cartLoaded;

        @SerializedName("Marketing")
        private String marketing;

        public String getToCart() {
            return toCart;
        }

        public void setToCart(String toCart) {
            this.toCart = toCart;
        }

        public String getCartLoaded() {
            return cartLoaded;
        }

        public void setCartLoaded(String cartLoaded) {
            this.cartLoaded = cartLoaded;
        }

        public String getMarketing() {
            return marketing;
        }

        public void setMarketing(String marketing) {
            this.marketing = marketing;
        }
    }

    public class Borders {
        @SerializedName("ButtonBorderRadius")
        private double borderRadius;

        @SerializedName("ButtonBorderWidth")
        private double borderWidth;

        public double getBorderRadius() {
            return borderRadius;
        }

        public void setBorderRadius(double borderRadius) {
            this.borderRadius = borderRadius;
        }

        public double getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(double borderWidth) {
            this.borderWidth = borderWidth;
        }
    }

    public class Colors {

        @SerializedName("BackgroundColor")
        private String backgroundColor;

        @SerializedName("ButtonBackgroundColor")
        private String buttonBackgroundColor;

        @SerializedName("ButtonBorderColor")
        private String buttonBorderColor;

        @SerializedName("ButtonFontColor")
        private String buttonFontColor;

        @SerializedName("MainFontColor")
        private String mainFontColor;

        @SerializedName("MarketingTint")
        private String marketingTint;

        public
        @ColorInt
        int getBackgroundColor(int defaultColor) {
            return Util.colorFromString(backgroundColor, defaultColor);
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public
        @ColorInt
        int getButtonBackgroundColor(int defaultColor) {
            return Util.colorFromString(buttonBackgroundColor, defaultColor);
        }

        public void setButtonBackgroundColor(String buttonBackgroundColor) {
            this.buttonBackgroundColor = buttonBackgroundColor;
        }

        public
        @ColorInt
        int getButtonBorderColor(int defaultColor) {
            return Util.colorFromString(buttonBorderColor, defaultColor);
        }

        public void setButtonBorderColor(String buttonBorderColor) {
            this.buttonBorderColor = buttonBorderColor;
        }

        public
        @ColorInt
        int getButtonFontColor(int defaultColor) {
            return Util.colorFromString(buttonFontColor, defaultColor);
        }

        public void setButtonFontColor(String buttonFontColor) {
            this.buttonFontColor = buttonFontColor;
        }

        public
        @ColorInt
        int getMainFontColor(int defaultColor) {
            return Util.colorFromString(mainFontColor, defaultColor);
        }

        public void setMainFontColor(String mainFontColor) {
            this.mainFontColor = mainFontColor;
        }

        public
        @ColorInt
        int getMarketingTint(int defaultColor) {
            return Util.colorFromString(marketingTint, defaultColor);
        }

        public void setMarketingTint(String marketingTint) {
            this.marketingTint = marketingTint;
        }
    }

}

