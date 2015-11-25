package com.mobium.config.prototype;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.config.Util;
import com.mobium.config.prototype_models.LeftMenuModel;
import com.mobium.config.prototype_models.NavigationBarModel;
import com.mobium.config.prototype_models.NavigationSection;
import com.mobium.reference.R;

import java.util.List;

/**
 *  on 19.11.15.
 */
class InterfaceModelBuilder {
    public static INavigationBar build(NavigationBarModel model, ConstantBuilder builder) {
        return new INavigationBar() {
            @Override
            public boolean uiCartEnabled() {
                return model.isUiCartEnabled();
            }

            @NonNull
            @Override
            public String getText(Context context) {
                return builder.getString(model.getUiText(), context);
            }

            @NonNull
            @Override
            public Gravity getGravity() {
                return Util.findEnum(Gravity.class, model.getUiGravity(), Gravity.left);
            }

            @NonNull
            @Override
            public Mode getMode() {
                return Util.findEnum(Mode.class, model.getUiMode(), Mode.text);
            }

            @NonNull
            @Override
            public HomeIcon getHomeIconType() {
                return Util.findEnum(HomeIcon.class, model.getUiHomeIcon(), HomeIcon.native_icon);
            }

            @Override
            public int getColorBackground(Context context) {
                return builder.getColor(model.getColorBackground(), context, R.color.application_color_accent);
            }

            @Override
            public int getColorText(Context context) {
                return builder.getColor(model.getColorText(), context, R.color.application_color_accent);
            }

            @Nullable
            @Override
            public Integer getNativeMenuColor(Context context) {
                if (model.getColorMenuIcon() != null)
                    return builder.getColor(model.getColorMenuIcon(), context, 0);
                return null;
            }

            @Nullable
            @Override
            public Integer getCustomMenuRes(Context context) {
                return builder.getDrawableRes(model.getColorMenuIcon(), context);
            }
        };
    }

    public static ILeftMenu build(LeftMenuModel model, ConstantBuilder builder) {
        return new ILeftMenu() {
            @Override
            public int getBackgroundColor(Context context) {
                return builder.getColor(model.getMenuBackgroundColor(), context, android.R.color.background_dark);
            }

            @Override
            public int getSeparatorColor(Context context) {
                return builder.getColor(model.getMenuBackgroundColor(), context, android.R.color.background_dark);
            }

            @Override
            public int getCellTextColor(Context context) {
                return builder.getColor(model.getNavigationColorCell(), context, android.R.color.background_dark);
            }

            @Override
            public int getRegionIcon(Context context) {
                return builder.getDrawableRes(model.getChangeRegionIcon(), context, R.drawable.location_black);
            }

            @Override
            public String getRegionEmpty(Context context) {
                return builder.getString(model.getChangeRegionTitleText(), context);
            }

            @Override
            public String getRegionDialogTitle(Context context) {
                return builder.getString(model.getChangeRegionDialogTitle(), context);
            }

            @Override
            public String getRegionDialogMessage(Context context) {
                return builder.getString(model.getChangeRegionDialogMessage(), context);
            }

            @Override
            public String getRegionDialogOk(Context context) {
                return builder.getString(model.getChangeRegionDialogOk(), context);
            }

            @Override
            public String getRegionDialogCancel(Context context) {
                return builder.getString(model.getChangeRegionDialogCancel(), context);
            }

            @Override
            public String searchHintText(Context context) {
                return builder.getString(model.getSearchHintText(), context);
            }

            @Override
            public int searchHintColor(Context context) {
                return builder.getColor(model.getSearchHintColor(), context, android.R.color.darker_gray);
            }

            @Override
            public List<ILeftMenuSection> getMenuSections(Context context) {
                return Stream.of(model.getNavigationSections())
                        .map(value -> build(value, builder, context))
                        .collect(Collectors.toList());
            }
        };
    }

    private static ILeftMenu.ILeftMenuSection build(NavigationSection section, ConstantBuilder builder, Context context) {
        return new ILeftMenu.ILeftMenuSection() {
            @Override
            public String getSectionTitle() {
                return builder.getString(section.getTitle(), context);
            }

            @Override
            public List<ILeftMenu.ILefMenuCell> getCells() {
                return Stream.of(section.getCells())
                        .map(value -> new ILeftMenu.ILefMenuCell(){

                            @Override
                            public String getTitle() {
                                return builder.getString(value.getTitle(), context);
                            }

                            @Override
                            public String getActionType() {
                                return value.getActionType();
                            }

                            @Override
                            public String getActionData() {
                                return value.getActionData();
                            }

                            @Override
                            public Drawable getIcon(Context context) {
                                return ActivityCompat.getDrawable(context, builder.getDrawableRes(value.getImage(), context, android.R.drawable.star_off));
                            }
                        }).collect(Collectors.toList());
            }
        };
    }

}
