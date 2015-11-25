package com.mobium.config.prototype;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

/**
 *  on 19.11.15.
 */
public interface ConstantBuilder {
    String getString(String key, Context context);
    @ColorInt Integer getColor(String key, Context context, @ColorRes int defColor);
    @DrawableRes Integer getDrawableRes(String key, Context context, @DrawableRes int defDrawable);

    @DrawableRes @Nullable Integer getDrawableRes(String key, Context context);
}
