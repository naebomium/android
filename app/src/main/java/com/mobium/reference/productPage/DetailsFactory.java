package com.mobium.reference.productPage;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.activity.MainDashboardActivity;

import java.util.List;

/**
 *   01.04.15.
 * http://mobiumapps.com/
 */
public class DetailsFactory {
    private final MainDashboardActivity activity;
    private final ShopItem shopItem;


    public DetailsFactory(MainDashboardActivity activity, ShopItem shopItem) {
        this.activity = activity;
        this.shopItem = shopItem;
    }

    public List<ProductDetailsBase> makeDetailsList(DetailsType... types) {
        return Stream.of(types).map(this::makeDetails).collect(Collectors.toList());
    }


    public ProductDetailsBase makeDetails(DetailsType type) {
        switch (type) {
            case PRODUCT_FEATURES:
                return new FeaturesDetails(activity, shopItem);
            case OPINIONS:
                return new ReviewDetails(activity, shopItem);
            case DESCRIPTION:
                return new DescriptionDetails(activity, shopItem);
            case OTHER_ITEMS:
                return new OtherItemsDetails(activity, shopItem);
            case RELATED_ITEMS:
                return new RelatedItems(activity, shopItem);
        }
        return new DescriptionDetails(activity, shopItem);
    }


    public List<ProductDetailsBase> makeDetailsList(List<DetailsType> types) {
        return Stream.of(types).map(this::makeDetails).collect(Collectors.toList());
    }


    public interface DetailsLifeCycle {
        void onPause();
        void onDestroy();
        void onResume();
    }
}
