package com.mobium.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.mobium.config.screen_models.Screen;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


/**
 *  on 29.10.15.
 */
public abstract class BaseShopConfiguration implements ShopConfiguration {
    private Screen mainScreen;
    private Screen mainMenu;
    private Screen productScreen;
    private Screens screens;

    protected BaseShopConfiguration(Context context, Gson gson) {

        ScreenParser parser = new ScreenParser();

        try {
            mainMenu = parser.parseScreen("left_menu.json", context.getApplicationContext(), gson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            productScreen = parser.parseScreen("k_t.json", context.getApplicationContext(), gson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mainScreen = parser.parseScreen("start_page.json", context.getApplicationContext(), gson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        screens = new Screens() {
            @Override
            public Screen getMainPageModel() {
                return mainScreen;
            }

            @Override
            public Screen getMenuModel() {
                return mainMenu;
            }

            @Override
            public Screen getProductPageModel() {
                return productScreen;
            }
        };
    }


    @NonNull
    @Override
    public Screens screens() {
        return screens;
    }

}
