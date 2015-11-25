package com.mobium.config.prototype;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.List;

/**
 *  on 18.11.2015.
 */
public interface ILeftMenu {
    @ColorInt int getBackgroundColor(Context context);
    @ColorInt int getSeparatorColor(Context context);
    @ColorInt int getCellTextColor(Context context);

    @DrawableRes int getRegionIcon(Context context);

    String getRegionEmpty(Context context);
    String getRegionDialogTitle(Context context);
    String getRegionDialogMessage(Context context);
    String getRegionDialogOk(Context context);
    String getRegionDialogCancel(Context context);

    String searchHintText(Context context);
    @ColorInt int searchHintColor(Context context);


    List<ILeftMenuSection> getMenuSections(Context context);

    interface ILeftMenuSection {
        String getSectionTitle();
        List<ILefMenuCell> getCells();
    }

    interface ILefMenuCell {
        String getTitle();
        String getActionType();
        String getActionData();
        Drawable getIcon(Context context);
    }
}


