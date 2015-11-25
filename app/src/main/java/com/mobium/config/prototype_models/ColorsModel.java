package com.mobium.config.prototype_models;

import android.content.Context;
import android.graphics.Color;

import com.mobium.config.Util;
import com.mobium.config.common.ConfigUtils;
import com.mobium.config.prototype.IColors;

import java.util.HashMap;

/**
 *  on 16.11.15.
 */
public class ColorsModel implements IColors {
    private HashMap<String, String> values;

    @Override
    public int getColorForName(Context context, String name) {
        return Util.colorFromString(values.get(name), context, android.R.color.transparent);
    }

    public ColorsModel() {
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }
}
