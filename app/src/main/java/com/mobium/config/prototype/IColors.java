package com.mobium.config.prototype;

import android.content.Context;
import android.support.annotation.ColorInt;

/**
 *  on 14.11.15.
 */
public interface IColors {
    @ColorInt int getColorForName(Context context, String name);
}
