package com.mobium.config.block_models;

import android.support.annotation.ColorInt;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;

/**
 *  on 02.11.2015.
 */
public class ButtonModel extends BlockModel {
    @SerializedName("Title")
    private String title;
    @SerializedName("ImagePath")
    private String imagePath;
    @SerializedName("BackgroundColor")
    private String backgroundColor;
    @SerializedName("TitleColor")
    private String titleColor;
    @SerializedName("BorderWidth")
    private int borderWidth;
    @SerializedName("BorderRadius")
    private int borderRadius;
    @SerializedName("ImagePosition")
    private IMAGE_POSITION imagePosition;
    @SerializedName("TitleEdgeInsets")
    private Insets titleEdgeInsets;
    @SerializedName("FontSize")
    private int fontSize;
    @SerializedName("TextAligment")
    private Alignment textAligment;
    @SerializedName("Data")
    private String data;
    @SerializedName("ActionType")
    private String actionType;

    public ButtonModel() {
        super(BlockType.BUTTON);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

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
    int getTitleColor(int defaultColor) {
        return Util.colorFromString(titleColor, defaultColor);
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    public IMAGE_POSITION getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(IMAGE_POSITION imagePosition) {
        this.imagePosition = imagePosition;
    }

    public Insets getTitleEdgeInsets() {
        return titleEdgeInsets;
    }

    public void setTitleEdgeInsets(Insets titleEdgeInsets) {
        this.titleEdgeInsets = titleEdgeInsets;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Alignment getTextAligment() {
        return textAligment;
    }

    public void setTextAligment(Alignment textAligment) {
        this.textAligment = textAligment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public enum IMAGE_POSITION {
        LEFT, RIGHT, BOTTOM, TOP
    }

}
