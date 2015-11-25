package com.mobium.config.block_views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.mobium.config.block_models.ImagesPagerModel;
import com.mobium.config.models.ImageItem;
import com.mobium.config.views.ImagesPagerView;
import com.mobium.new_api.models.Banner;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.config.common_views.BannerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  on 29.10.2015.
 */
public class ImagesPager extends BaseView<ImagesPagerModel> implements ImagesPagerView {
    private BannerView bannerView;
    private List<ImageItem> data;

    public ImagesPager(ImagesPagerModel model) {
        super(model);
    }

    @Override
    protected View buildView(Context context, @NonNull ViewGroup viewGroup, boolean add) {
        bannerView = new BannerView(context);
        bannerView.setActiveColor(model.getSelectedPageIndicatorColor(Color.BLACK));
        bannerView.setNotActiveColor(model.getPageIndicatorColor(Color.WHITE));
        bannerView.setProgressBarColor(model.getSpinnerStyle().getColor());
        bannerView.setLayoutParams(getDefaultParams());
        if (add) {
            viewGroup.addView(bannerView);
        }
        bannerView.calculateHeight(model.getBannerHeightRatio());
        if (data != null)
            setData(data);
        return bannerView;
    }


    @Override
    public void setData(List<ImageItem> data) {
        this.data = data;
        bannerView.setBannerList((FragmentActivity) bannerView.getContext(), data, 5000);
        bannerView.hideProgress();
    }

    @Override
    public boolean hasData() {
        return data != null;
    }

    @Override
    public void onEmptyData() {
        bannerView.setVisibility(View.GONE);
    }
}
