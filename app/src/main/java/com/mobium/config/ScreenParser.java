package com.mobium.config;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.mobium.config.screen_models.Screen;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  on 30.10.15.
 */
public class ScreenParser {

    public Screen parseScreen(String resourceName, Context context, Gson gson) throws IOException, JsonIOException, JsonSyntaxException {
        JsonReader reader = loadJSONFromAsset(context.getAssets(), resourceName);
        Screen screen = gson.fromJson(reader, Screen.class);
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screen;
    }

    public JsonReader loadJSONFromAsset(AssetManager manager, String filename) throws IOException {
        return new JsonReader(new InputStreamReader(manager.open(filename)));
    }

}
