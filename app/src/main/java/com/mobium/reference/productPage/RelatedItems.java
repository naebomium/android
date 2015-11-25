package com.mobium.reference.productPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.activity.MainDashboardActivity;

/**
 *  on 10.04.15.
 * http://mobiumapps.com/
 */
public class RelatedItems extends OtherItemsDetails {

    public RelatedItems(MainDashboardActivity context, ShopItem item) {
        super(context, item);
        setType(DetailsType.RELATED_ITEMS);
    }

    protected void setItems() {
        items = shopItem.detailsInfo.relatedItems;
    }

    @Override
    protected View fillContentWrapper(LayoutInflater inflater, ViewGroup contentWrapper) {
        return super.fillContentWrapper(inflater, contentWrapper);
    }
}
