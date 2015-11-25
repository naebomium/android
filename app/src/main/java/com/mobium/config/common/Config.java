package com.mobium.config.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mobium.config.BaseShopConfiguration;
import com.mobium.config.Screens;
import com.mobium.config.ShopConfiguration;
import com.mobium.config.StringConstants;
import com.mobium.config.block_models.BlockModel;
import com.mobium.config.converters.BlockConverter;
import com.mobium.config.prototype.ConfigurationImplementation;
import com.mobium.config.prototype.IConfiguration;
import com.mobium.config.prototype_models.ConfigurationModel;
import com.mobium.config.screen_models.Screen;
import com.mobium.reference.R;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  on 04.08.15.
 * http://mobiumapps.com/
 */
public class Config {
    private static IConfiguration configuration;


    public static void init(Context context) {
        if (configuration == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BlockModel.class, new BlockConverter())
                    .create();

            JsonReader reader = null;
            ConfigurationModel model = null;
            try {
                reader = new JsonReader(new InputStreamReader(context.getAssets().open("config.json")));
                model = gson.fromJson(reader, ConfigurationModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            configuration = new ConfigurationImplementation(model);
        }

    }

    public static IConfiguration get() {
        return configuration;
    }



}
