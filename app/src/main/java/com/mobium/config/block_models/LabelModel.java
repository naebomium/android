package com.mobium.config.block_models;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;

/**
 *  on 30.10.15.
 */
public class LabelModel extends BlockModel {
    private @SerializedName("Alignment") Alignment alignment;
    private @SerializedName("TextColor") String textColor;
    private @SerializedName("BackgroundColor") String backgroundColor;
    private @SerializedName("MaxLines") int maxLines;
    private @SerializedName("Text") String text;
    private @SerializedName("IndependentSize")
    Inset size;

    public LabelModel() {
        super(BlockType.LABEL);
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextColor() {
        return textColor;
    }

    public @ColorInt int getBackgroundColor(@ColorInt int defaultColor) {
        return Util.colorFromString(backgroundColor, defaultColor);
    }

    public @ColorInt int getBackgroundColor(Context context, @ColorRes int defaultColor) {
        return Util.colorFromString(backgroundColor, context, defaultColor);
    }

    public @ColorInt int getTextColor(@ColorInt int defaultColor) {
        return Util.colorFromString(textColor, defaultColor);
    }

    public @ColorInt int getTextColor(Context context, @ColorRes int defaultColor) {
        return Util.colorFromString(textColor, context, defaultColor);
    }

    public int getMaxLines() {
        return maxLines;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public Inset getSize() {
        return size;
    }

    public void setSize(Inset size) {
        this.size = size;
    }

}
