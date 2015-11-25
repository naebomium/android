package com.mobium.config.prototype;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.config.StringConstants;
import com.mobium.config.Util;
import com.mobium.config.prototype_models.ConfigurationModel;
import com.mobium.config.prototype_models.DesignModel;
import com.mobium.config.prototype_models.LeftMenuModel;

import java.util.List;
import java.util.Set;

/**
 *  on 16.11.15.
 */
public class ConfigurationImplementation implements IConfiguration {
    private final IStaticData staticData; // ok
    private final IApplicationData applicationData; //ok
    private final IColors colors; // ok
    private final IDesign design; //todo::make
    private final ConfigurationModel model; // ok
    private final ConstantBuilder builder;

    public ConfigurationImplementation(ConfigurationModel model) {
        staticData = model.getStaticData();
        applicationData = model.getApplicationDataModel();
        colors = model.getColorsModel();
        design = make(model.getDesignModel());
        this.model = model;

        builder = new ConstantBuilder() {
            @Override
            public String getString(String key, Context context) {
                return Optional.ofNullable(model.getTranslations().get(0).getStrings().get(key)).orElse("No string for " + key);
            }

            @Override
            public Integer getColor(String key, Context context, @ColorRes int defColor) {
                return Util.colorFromString(key, context, defColor);
            }

            @Override
            public Integer getDrawableRes(String key, Context context, @DrawableRes int defDrawable) {
                return Util.drawableResByName(key, context, defDrawable);
            }

            @Nullable
            @Override
            public Integer getDrawableRes(String key, Context context) {
                return Util.drawableResByName(key, context);
            }
        };
    }

    private IDesign make(DesignModel designModel) {
        return new IDesign() {
            @NonNull
            @Override
            public INavigationBar defaultNavigationBar() {
                return InterfaceModelBuilder.build(designModel.getModel(), builder);
            }

            @NonNull
            @Override
            public INavigationBar getNavigationBarForSreen(IScreen screen) {
                return null;
            }

            @NonNull
            @Override
            public Set<IScreen> screens() {
                return null;
            }

            @NonNull
            @Override
            public ILeftMenu getLeftMenu() {
                return InterfaceModelBuilder.build(designModel.getLeftMenuModel(), builder);
            }
        };
    }


    @NonNull
    @Override
    public IStaticData getStaticData() {
        return staticData;
    }

    @NonNull
    @Override
    public IApplicationData getApplicationData() {
        return applicationData;
    }

    @NonNull
    @Override
    public IColors getColors() {
        return colors;
    }

    @NonNull
    @Override
    public IDesign design() {
        return design;
    }

    @Override
    public boolean logDebugInfo() {
        return true;
    }

    @NonNull
    @Override
    public StringConstants strings() {
        return new StringConstants() {
            @Override
            public String getMainPageTitle() {
                return null;
            }

            @Override
            public String otherItemsTitle() {
                return null;
            }

            @Override
            public String relatedItemsTitle() {
                return null;
            }

            @Override
            public String preCartTitle() {
                return null;
            }

            @Override
            public String getCurrencyPlaceholder() {
                return null;
            }

            @Override
            public String noInternetErrorMessage() {
                return null;
            }

            @Override
            public String regionNotSelectedErrorMessage() {
                return null;
            }

            @Override
            public String autoCompleteRegionTitle() {
                return null;
            }

            @Override
            public String autoCompleteRegionMessage() {
                return null;
            }

            @Override
            public String getCheckoutTitle() {
                return null;
            }

            @Override
            public String getForgetPasswordUrl() {
                return null;
            }

            @Override
            public String getYoutubeUrl() {
                return null;
            }

            @Override
            public String getFbUrl() {
                return null;
            }

            @Override
            public String getVkUrl() {
                return null;
            }

            @Override
            public String getInstagammUrl() {
                return null;
            }
        };
    }
}
