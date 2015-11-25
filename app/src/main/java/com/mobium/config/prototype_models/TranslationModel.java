package com.mobium.config.prototype_models;

import java.util.HashMap;

/**
 *  on 16.11.15.
 */
public class TranslationModel {
    public String lang_code;
    public HashMap<String, String> strings;

    public TranslationModel() {
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public HashMap<String, String> getStrings() {
        return strings;
    }

    public void setStrings(HashMap<String, String> strings) {
        this.strings = strings;
    }
}
