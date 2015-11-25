package com.mobium.config.prototype_models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *  on 16.11.15.
 */
public class ConfigurationModel {
    private @SerializedName("static_data") StaticDataModel staticData; // ok
    private @SerializedName("application_data") ApplicationDataModel applicationDataModel; // ok
    private @SerializedName("colors") ColorsModel colorsModel; // todo:: makeMobiumProfileApi
    private @SerializedName("translations") ArrayList<TranslationModel> translations; // ok
    private @SerializedName("design") DesignModel designModel; //todo:: make

    public ConfigurationModel() {
    }

    public StaticDataModel getStaticData() {
        return staticData;
    }

    public void setStaticData(StaticDataModel staticData) {
        this.staticData = staticData;
    }

    public ApplicationDataModel getApplicationDataModel() {
        return applicationDataModel;
    }

    public void setApplicationDataModel(ApplicationDataModel applicationDataModel) {
        this.applicationDataModel = applicationDataModel;
    }

    public ColorsModel getColorsModel() {
        return colorsModel;
    }

    public void setColorsModel(ColorsModel colorsModel) {
        this.colorsModel = colorsModel;
    }

    public ArrayList<TranslationModel> getTranslations() {
        return translations;
    }

    public void setTranslations(ArrayList<TranslationModel> translations) {
        this.translations = translations;
    }

    public DesignModel getDesignModel() {
        return designModel;
    }

    public void setDesignModel(DesignModel designModel) {
        this.designModel = designModel;
    }
}
