package com.mobium.config;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;

import com.annimon.stream.Optional;
import com.mobium.config.block_models.Alignment;

/**
 *  on 30.10.15.
 */
public class Util {
    private static int parseColor(String color) throws IllegalArgumentException {
        int colorSize = color.length();
        if (colorSize == 9) {
            String argb = "#" + color.substring(7) + color.substring(1, 7);
            return Color.parseColor(argb);
        } else if (colorSize == 7) {
            return Color.parseColor(color);
        } else  throw new IllegalArgumentException("Unknown color");
    }

    public static int colorFromString(String color, @ColorInt int defaultValue) {
        try {
            return parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static int colorFromString(String color, Context context, @ColorRes int defaultResource) {
        try {
            return parseColor(color);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                return ActivityCompat.getColor(context, defaultResource);
            } catch (Exception e2) {
                e2.printStackTrace();
                return Color.BLACK;
            }
        }
    }

    public static String makeFormatFree(String image) {
        return image.replace(".png", "");
    }

    public static @Nullable Drawable drawableByName(String name, Context context) {
        try {
            int resource = context.getResources().getIdentifier(makeFormatFree(name), "drawable", context.getPackageName());
            return ActivityCompat.getDrawable(context, resource);
        } catch (Exception e) {
            return null;
        }
    }

    public static Optional<Integer> getDrawableRes(String name, Context context) {
        try {
            return Optional.of(context.getResources().getIdentifier(makeFormatFree(name), "drawable", context.getPackageName()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Nullable
    public static Integer drawableResByName(String name, Context context) {
        try {
            return context.getResources().getIdentifier(makeFormatFree(name), "drawable", context.getPackageName());
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static Integer drawableResByName(String name, Context context, @DrawableRes int defaultValue) {
        try {
            return  context.getResources().getIdentifier(makeFormatFree(name), "drawable", context.getPackageName());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Drawable drawableByName(String name, Context context, @DrawableRes int defaultValue) {
        try {
            int resource = context.getResources().getIdentifier(makeFormatFree(name), "drawable", context.getPackageName());
            return ActivityCompat.getDrawable(context, resource);
        } catch (Exception e) {
            return ActivityCompat.getDrawable(context, defaultValue);
        }
    }

    public static <T extends Enum<T>> T findEnum(Class<T> enumType, String name, T def) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (Exception e) {
            return def;
        }
    }

    public static int getGravityByAlignment(Alignment alignment) {
        if (alignment == null)
            return Gravity.LEFT;

        switch (alignment) {
            case LEFT:
                return Gravity.LEFT;
            case CENTER:
                return Gravity.CENTER;
            case RIGHT:
                return Gravity.RIGHT;
            default:
                return Gravity.LEFT;
        }
    }
}
