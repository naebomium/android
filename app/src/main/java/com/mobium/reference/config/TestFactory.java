package com.mobium.reference.config;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.mobium.config.common.DataExchangeException;
import com.mobium.config.common.LoadableView;
import com.mobium.config.common.Provider;
import com.mobium.config.common.ProviderFactory;
import com.mobium.config.common.UpdatableLoadableView;
import com.mobium.config.common.UpdatesProvider;
import com.mobium.config.models.ImageItem;
import com.mobium.config.views.ImagesPagerView;
import com.mobium.reference.utils.Actions;

import java.util.Arrays;
import java.util.List;

/**
 *  on 03.11.15.
 */
public class TestFactory implements ProviderFactory {
    @Override
    public <T> Provider<T> get(FragmentActivity activity, LoadableView<T> view) {
        if (view instanceof ImagesPagerView) {
            return () -> (T) Arrays.asList(new ImageItem() {
                @Override
                public String imageUrl() {
                    return "http://img.sur.ly/thumbnails/620x343/m/mobiumapps.com.png";
                }

                @Override
                public View.OnClickListener onClick() {
                    return v -> Actions.doUrlExternal((FragmentActivity) v.getContext(), "http://mobiumapps.com/");
                }
            });
        }
        return null;
    }

    @Override
    public <T> UpdatesProvider<T> get(FragmentActivity activity, UpdatableLoadableView<T> view) {
        return null;
    }
}
