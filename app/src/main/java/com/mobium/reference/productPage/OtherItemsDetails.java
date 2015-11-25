package com.mobium.reference.productPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;

/**
 *  on 10.04.15.
 * http://mobiumapps.com/
 */
public class OtherItemsDetails extends ProductDetailsBase {
    protected ShopItem[] items;

    public OtherItemsDetails(MainDashboardActivity context, ShopItem item) {
        super(context, DetailsType.OTHER_ITEMS, item);
    }


    protected void setItems() {
        items = shopItem.detailsInfo.otherItems;
    }

    @Override
    protected View fillContentWrapper(final LayoutInflater inflater, ViewGroup contentWrapper) {
        setItems();

        View resultView = inflater.inflate(R.layout.fragment_product_details_analogues, contentWrapper, true);
        android.support.v7.widget.GridLayout viewItems = (android.support.v7.widget.GridLayout) resultView.findViewById(R.id.product_analogues_items);

        viewItems.setColumnCount(2);

        for (ShopItem item : items) {

            View view = inflater.inflate(R.layout.shop_item_card, viewItems, false);
            view.getLayoutParams().width = 0;
            WebImageView icon = (WebImageView) view.findViewById(R.id.image);
            TextView title = (TextView) view.findViewById(R.id.label);
            TextView cost = (TextView) view.findViewById(R.id.cost);
            TextView oldCost = (TextView) view.findViewById(R.id.oldCost);

            Ui_configurator.getInstance(activity).configureShopItemInfo(item, icon, title, cost, oldCost, null, null);
            Ui_configurator.getInstance(activity).configureOnShopItemClicks(view.findViewById(R.id.frame), item);
            viewItems.addView(view);
        }



        return resultView;
    }

    @Override
    protected boolean needAddButtons() {
        return false;
    }
}
