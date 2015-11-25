package com.mobium.config.common_views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobium.client.models.Action;
import com.mobium.config.models.ImageItem;
import com.mobium.new_api.models.Banner;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.views.DotedViewPager;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 */
public class BannerView extends DotedViewPager {
    private List<ImageItem> bannerList;
    private final Handler handler;
    private long milliSecDelay;

    {
        handler = new Handler();
    }

    private boolean pageChangedByUser = false;

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBannerList(final FragmentActivity activity, List<ImageItem> banners, long milliSecToDelay) {
        int maxSizeOfBanner = 15;

        bannerList = banners.size() > maxSizeOfBanner ?
                banners.subList(0, maxSizeOfBanner) :
                banners;

        setActiveColor(getResources().getColor(R.color.application_color_accent));
        setNotActiveColor(getResources().getColor(R.color.dark_gray));

        if (bannerList.size() == 0) {
            pager.setVisibility(GONE);
            dotLayout.setVisibility(GONE);
            pager.setAdapter(null);
            return;
        }

        pager.setOffscreenPageLimit(bannerList.size());

        pager.setVisibility(VISIBLE);
        dotLayout.setVisibility(VISIBLE);

        setAdapter(new PagerAdapter() {
            private ViewGroup.LayoutParams lp =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

            @Override
            public int getCount() {
                return bannerList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return com.annimon.stream.Objects.equals(view, object);
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                WebImageView imageView = new WebImageView(activity);
                final ImageItem item = bannerList.get(position);

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                Picasso.with(activity)
                        .load(item.imageUrl())
                        .error(Ui_configurator.getErrorImage(activity))
//                        .fetch()
                        .into(imageView);

                imageView.setOnClickListener(view -> {
//                    activity.onEvent(StatisticsEvent.TAB_BANNER,
//                            "banner_id", item.id,
//                            "banner_content_type", item.getActionType(),
//                            "banner_content_data", item.getActionData());
                    item.onClick().onClick(imageView);
                });

                container.addView(imageView, lp);
                return imageView;
            }
        });

        this.milliSecDelay = milliSecToDelay;
        handler.postDelayed(this::changePage, milliSecToDelay);
        setListener(i -> setPageChangedByUser(true));
    }

    public boolean isPageChangedByUser() {
        return pageChangedByUser;
    }

    public void setPageChangedByUser(boolean pageChangedByUser) {
        this.pageChangedByUser = pageChangedByUser;
    }

    private void changePage() {
        if (pager.getAdapter() == null)
            return;
        if (isPageChangedByUser()) {
            setPageChangedByUser(false);
            handler.postDelayed(this::changePage, milliSecDelay);
            return;
        }
        int count = pager.getAdapter().getCount();
        int current = pager.getCurrentItem();
        if (count > 1) {
            if (current == count - 1)
                pager.setCurrentItem(0, false);
            else
                pager.setCurrentItem(current + 1, true);
            handler.postDelayed(this::changePage, milliSecDelay);
        }
    }


    public void calculateHeight(double heightRatio) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        int height = (int) (displaymetrics.widthPixels * heightRatio);
        setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
        ));
    }
}
