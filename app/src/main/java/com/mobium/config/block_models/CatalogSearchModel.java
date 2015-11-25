package com.mobium.config.block_models;

import android.support.annotation.ColorInt;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;

/**
 *  on 30.10.15.
 */
public class CatalogSearchModel extends BlockModel {
    private @SerializedName("BarcodeSearchEnabled") boolean barcodeEnabled;
    private @SerializedName("VoiceSearchEnabled") boolean voiceSearchEnabled;
    private @SerializedName("BackgroundColor") String backgroundColor;
    private @SerializedName("TextColor") String textColor;
    private @SerializedName("TextAreaColor") String textAreaBackgroundColor;
    private @SerializedName("Placeholder") String hint;
    private @SerializedName("ScanIcon") String scanIcon;
    private @SerializedName("VoiceIcon") String voiceIcon;
    private @SerializedName("HintColor") String hintColor;


    {
        hint = "Начать поиск";
    }
    public CatalogSearchModel() {
        super(BlockType.SEARCH_BAR);
    }

    public void setBarcodeEnabled(boolean barcodeEnabled) {
        this.barcodeEnabled = barcodeEnabled;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isBarcodeEnabled() {
        return barcodeEnabled;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTextAreaBackgroundColor() {
        return textAreaBackgroundColor;
    }

    public void setTextAreaBackgroundColor(String textAreaBackgroundColor) {
        this.textAreaBackgroundColor = textAreaBackgroundColor;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getScanIcon() {
        return scanIcon;
    }

    public void setScanIcon(String scanIcon) {
        this.scanIcon = scanIcon;
    }

    public boolean isVoiceSearchEnabled() {
        return voiceSearchEnabled;
    }

    public void setVoiceSearchEnabled(boolean voiceSearchEnabled) {
        this.voiceSearchEnabled = voiceSearchEnabled;
    }

    public String getVoiceIcon() {
        return voiceIcon;
    }

    public void setVoiceIcon(String voiceIcon) {
        this.voiceIcon = voiceIcon;
    }

    public @ColorInt
    int getHintColor(@ColorInt int defaultColor) {
        return Util.colorFromString(hintColor, defaultColor);
    }

    public void setHintColor(String hintColor) {
        this.hintColor = hintColor;
    }
}
