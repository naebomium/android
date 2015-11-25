package com.mobium.reference.config;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.LruCache;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.config.block_models.CollectionViewModel;
import com.mobium.config.block_views.CollectionView;
import com.mobium.config.common.ConfigUtils;
import com.mobium.config.common.DataExchangeException;
import com.mobium.config.common.LoadableView;
import com.mobium.config.common.Provider;
import com.mobium.config.common.UpdatableLoadableView;
import com.mobium.config.common.UpdatesProvider;
import com.mobium.config.models.ImageItem;
import com.mobium.config.models.ItemWithCost;
import com.mobium.config.models.ItemWithName;
import com.mobium.config.views.ImagesPagerView;
import com.mobium.new_api.Api;
import com.mobium.new_api.models.BannerList;
import com.mobium.new_api.models.Response;
import com.mobium.new_api.models.catalog.PopularCategory;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;

import java.util.Arrays;
import java.util.List;

import retrofit.RetrofitError;

/**
 *  on 03.11.15.
 */
public class ProviderFactoryIml implements com.mobium.config.common.ProviderFactory {
    final LruCache<String, Object> lruCache = new LruCache<>(1024);
    private static final String discount = "Discount";
    private static final String popularCategory = "popularCategory";

    @Override
    public <T> Provider<T> get(FragmentActivity activity, LoadableView<T> view) {
        if (view instanceof ImagesPagerView) {
            Provider<List<ImageItem>> provider = () -> {
                try {
                    return ProviderFactoryIml.this.getImages(activity);
                } catch (RetrofitError error) {
                    throw new DataExchangeException(error.getMessage(), false);
                }
            };
            return (Provider<T>) provider;
        }
        return null;
    }

    @Override
    public <T> UpdatesProvider<T> get(FragmentActivity activity, UpdatableLoadableView<T> view) {
        if (view instanceof CollectionView) {
            CollectionView collectionView = (CollectionView) view;
            CollectionViewModel model = (CollectionViewModel) collectionView.getModel();
            if (model.getContentSource().equals(CollectionViewModel.ContentSource.API_METHOD)) {
                if (model.getApiMethod().equals(CollectionViewModel.API_METHOD.MARKETING_ITEMS)) {
                    return (limit, offset) -> {
                        try {
                            return (T) loadDiscount(limit, offset);
                        } catch (ExecutingException e) {
                            e.printStackTrace();
                            throw new DataExchangeException(e.getUserMessage(), e.isCanRepeat());
                        }
                    };
                } else if (model.getApiMethod().equals(CollectionViewModel.API_METHOD.POPULAR_CATEGORIES)) {
                    return (limit, offset) -> {
                        try {
                            return (T) loadPopularCategory(limit, offset);
                        } catch (ExecutingException e) {
                            e.printStackTrace();
                            throw new DataExchangeException(e.getUserMessage(), e.isCanRepeat());
                        }
                    };
                }
            }
        }

        return null;
    }


    private List<ItemWithCost> loadDiscount(int limit, int offset) throws ExecutingException {
        List<ItemWithCost> cache = (List<ItemWithCost>) lruCache.get(discount);
        if (cache == null) {
            ShopItem shopItems[] = ReferenceApplication.getInstance().getExecutor().loadDiscounts();
            cache = mapToItemWithCost(Arrays.asList(shopItems));
            lruCache.put(discount, cache);
        }
        return cache;
    }


    private List<ItemWithName> loadPopularCategory(int limit, int offset) throws ExecutingException {
        List<ItemWithName> cache = (List<ItemWithName>) lruCache.get(popularCategory);
        if (cache == null) {
            ShopCategory category[] = ReferenceApplication.getInstance().getExecutor().loadPopularCategories(ShopDataStorage.getRegionId());
            cache = mapToItemWithName(Arrays.asList(category));
            lruCache.put(popularCategory, cache);
        }
        return cache;
    }

    private List<ImageItem> getImages(FragmentActivity activity) throws RetrofitError, DataExchangeException {
        Response<BannerList> listResponse = Api.getInstance().getBaners();
        if (!listResponse.success())
            throw new DataExchangeException(listResponse.error().description, listResponse.error().mayRetry);
        return Stream.of(listResponse.result.banners)
                .map(value -> new ImageItem() {
                    @Override
                    public String imageUrl() {
                        return value.getImage().getHd();
                    }

                    @Override
                    public View.OnClickListener onClick() {
                        return v -> {
                            FragmentActionHandler.doAction(activity, new Action(value.getActionType(), value.getActionData()));
                            Events.get((Activity) v.getContext()).banners().onBannerSelect(value);
                        };
                    }
                })
                .collect(Collectors.toList());
    }



    private static List<ItemWithCost> mapToItemWithCost(List<ShopItem> shopItems) {
        return
                Stream.of(shopItems)
                .map(value -> new ItemWithCost() {
                    @Override
                    public String name() {
                        return value.title;
                    }

                    @Override
                    public String cost() {
                        return ConfigUtils.formatCurrency(value.cost.getCurrentConst());
                    }

                    @Nullable
                    @Override
                    public String oldCost() {
                        return value.cost.getOldCost() > 0 ? ConfigUtils.formatCurrency(value.cost.getOldCost()) : null;
                    }

                    @Override
                    public String iconUrl() {
                        return value.getIcon().map(value1 -> value1.getUrl("sd")).orElse(null);
                    }

                    @Override
                    public Action onClick() {
                        return new Action(ActionType.OPEN_PRODUCT, value.getId());
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<ItemWithName> mapToItemWithName(List<ShopCategory> categories) {
        return Stream.of(categories)
                .map(value -> new ItemWithName() {
                    @Override
                    public String name() {
                        return value.title;
                    }

                    @Override
                    public String url() {
                        return value.getIcon().map(icon -> icon.getUrl("sd")).orElse(null);
                    }

                    @Override
                    public Action onClick() {
                        return new Action(ActionType.OPEN_CATEGORY, value.id);
                    }
                })
                .collect(Collectors.toList());
    }
}
