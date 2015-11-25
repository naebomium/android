package com.mobium.reference.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.new_api.Api;
import com.mobium.new_api.SimpleHandler;
import com.mobium.new_api.models.Banner;
import com.mobium.new_api.models.BannerList;
import com.mobium.new_api.models.MobiumError;
import com.mobium.reference.R;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.Receivers;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.config.common_views.BannerView;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;
import com.mobium.reference.views.adapters.LoadAdapter;
import com.mobium.reference.views.adapters.ProductAdapter;
import com.mobium.reference.views.adapters.TopCategoryAdapter;

import java.util.Arrays;

import static com.mobium.reference.utils.FragmentActionHandler.doAction;

/**
 *
 *
 * Date: 28.11.12
 * Time: 12:09
 */
public class StartFragment extends BasicLoadableFragment implements
        Receivers.BannerReceiver,
        Receivers.PopularCategoryReceiver,
        Receivers.SalesReceiver {
    public final static String TAG = "com.mobium.reference.fragments.StartFragment";
    public static final String CURRENT_ITEM = "com.mobium.reference.fragments.StartFragment.Banner";
    private static final double BANNER_RATIO = 0.55625;

    private int currentBannerItem;

    private ScrollView scrollView;
    private BannerView bannerView;

    private ShopCategory[] popularCategories;
    private ShopItem[] discountItems;

    private TextView salesTitle;
    private RecyclerView salesScrollView;

    private TextView topCategoryTitle;
    private RecyclerView topCategoryScrollsView;



    @Override
    protected boolean needLoading() {
        return true;
    }

    @Override
    protected boolean isSilentLoading() {
        return true;
    }

    @Override
    protected void onSilentError(ExecutingException e) {
        new Handler().postDelayed(this::performLoading, 1500);
    }

    @Override
    protected void loadInBackground() throws ExecutingException {
        discountItems = getApplication().getShopCache().loadDiscounts();
        popularCategories = getApplication().getShopCache().loadPopularCategories();
    }


    private void bannerTask() {

        bannerView.showProgress();

        Api api = Api.getInstance();

        api.GetBanners().call(new SimpleHandler<BannerList>() {
            @Override
            public void onResult(BannerList b) {
                if (isFragmentUIActive())
                    onBannerLoad(b.banners);
            }

            @Override
            public void onBadArgument(MobiumError mobiumError) {
                if (mobiumError.mayRetry && isFragmentUIActive())
                    api.GetBanners().call(this);
            }

            @Override
            public void onException(Exception ex) {
                if (isFragmentUIActive())
                    api.GetBanners().call(this);
            }
        });
    }


    @Override
    protected void afterPrepared() {
        onCategoryLoad(popularCategories);
        onSalesLoad(discountItems);
    }



    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putSerializable(CURRENT_ITEM, bannerView.getCurrent());
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        currentBannerItem = savedInstanceState.getInt(CURRENT_ITEM);
    }

    @Override
    public void onStart() {
        super.onStart();
        bannerTask();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.main.name());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_start, container, false);

        scrollView = (ScrollView) res.findViewById(R.id.scroll_view);
        bannerView = (BannerView) res.findViewById(R.id.fragment_start_banners);

        res.findViewById(R.id.fragment_start_banners_wrapper)
                .getLayoutParams().height = calculateBannerHeight();

        salesTitle = (TextView) res
                .findViewById(R.id.fragment_start_sales_title);
        topCategoryTitle = (TextView) res
                .findViewById(R.id.fragment_start_top_categories_title);
        salesScrollView =  (RecyclerView) res
                .findViewById(R.id.fragment_start_sales);
        topCategoryScrollsView = (RecyclerView) res
                .findViewById(R.id.fragment_start_top_categories);

        res.findViewById(R.id.fragment_product_shops_button)
                .setOnClickListener(v -> FragmentUtils.doOpenFragmentDependsOnInternetAccess(getActivity(), ConfigScreen.class, true));

        res.findViewById(R.id.fragment_start_open_catalog)
                .setOnClickListener(v -> doAction(getDashboardActivity(), new Action(ActionType.OPEN_CATALOG, null)));

        res.findViewById(R.id.fragment_start_open_sales)
                .setOnClickListener(v -> doAction(getDashboardActivity(), new Action(ActionType.OPEN_CATEGORY_BY_ALIAS, "actions")));

        RecyclerView.Adapter waitingAdapter = new LoadAdapter(3, null);

        topCategoryScrollsView.setAdapter(waitingAdapter);
        salesScrollView.setAdapter(waitingAdapter);

        RecyclerView.OnScrollListener scrollListener = new WebImageView.StopLoadScrollListener();

        salesScrollView.addOnScrollListener(scrollListener);
        topCategoryScrollsView.addOnScrollListener(scrollListener);

        topCategoryScrollsView.setLayoutManager(
                new LinearLayoutManager(getDashboardActivity(), LinearLayoutManager.HORIZONTAL, false)
        );
        salesScrollView.setLayoutManager(
                new LinearLayoutManager(getDashboardActivity(), LinearLayoutManager.HORIZONTAL, false)
        );

        bannerView.hideProgress();

        return res;
    }


    private int calculateBannerHeight() {
        int screenWidth = getScreenWidth();
        return (int) (screenWidth * BANNER_RATIO);
    }


    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    @Override
    public boolean isTopActionBarButtonMenu() {
        return true;
    }


    @Override
    public void onCategoryLoad(ShopCategory[] categories) {
        if (categories != null && categories.length != 0) {
            topCategoryScrollsView.setAdapter(
                    new TopCategoryAdapter(getDashboardActivity(), Arrays.asList(categories))
            );
        } else {
            topCategoryScrollsView.setVisibility(View.GONE);
            topCategoryTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSalesLoad(ShopItem[] items) {
        if (items != null && items.length != 0) {
            salesScrollView.setAdapter(
                    new ProductAdapter(getActivity(), Ui_configurator.getInstance(getActivity()), Arrays.asList(items))
            );
        } else {
            salesScrollView.setVisibility(View.GONE);
            salesTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBannerLoad(Banner[] banners) {
        if (banners != null && banners.length > 0) {
//            bannerView.setBannerList(
//                    getDashboardActivity(),
//                    Arrays.asList(banners),
//                    1000 * 3
//            );
            bannerView.setCurrent(currentBannerItem);
            bannerView.getBackgroundImageView().setOnClickListener(view -> doAction(getDashboardActivity(),new Action(ActionType.OPEN_CATALOG, null)));
            bannerView.hideProgress();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topCategoryScrollsView.clearOnScrollListeners();
    }
}
