package com.mobium.config.prototype;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *  on 14.11.15.
 */
public interface INavigationBar {
    boolean uiCartEnabled();

    @NonNull String getText(Context context);

    @NonNull Gravity getGravity();
    @NonNull Mode getMode();
    @NonNull HomeIcon getHomeIconType();


    @ColorInt int getColorBackground(Context context);
    @ColorInt int getColorText(Context context);

    @Nullable @ColorInt Integer getNativeMenuColor(Context context);
    @Nullable @DrawableRes Integer getCustomMenuRes(Context context);

    enum Gravity {
       left, center, right
    }

    enum Mode {
        icon_text, icon, text
    }

    enum HomeIcon {
        native_icon, custom_icon
    }

    String format =
            "\"color_background\" : \"#11223344\",\n" +
                    "            \"color_menu_icon\" : \"colors_main\",\n" +
                    "            \"color_text\" : \"colors_text_color\",\n" +
                    "            \"ui_cart_enabled\" : false,\n" +
                    "            \"ui_gravity\" : \"center\",\n" +
                    "            \"ui_text\" : \"checkout_screen_title\",\n" +
                    "            \"ui_icon\" : \"action_bar_title.png\",\n" +
                    "            \"ui_mode\" : \"icon_text\"";
}
