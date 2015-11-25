package com.mobium.reference.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.Price;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.config.common.ConfigUtils;
import com.mobium.reference.views.WebImageView;

import java.util.List;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class OrderItemsAdapter extends ListClickableAdapter<OrderItemsAdapter.Holder, CartItem> {

    public interface ClickListener {
        void onClick(CartItem item);
    }

    ReferenceApplication application;
    ClickListener listener;

    public OrderItemsAdapter(List<CartItem> items, Context context, @NonNull ClickListener l) {
        super(items, context);
        application = ReferenceApplication.getInstance();
        listener = l;
    }

    @Override
    protected void applyItemToHolder(Holder h, CartItem item, int itemPosition) {
        int count = item.count;
        //cost = item from user profile ? checkout time cost : current time cost != null ? current time cost : 0 ;
        int cost = Optional.ofNullable(item.shopItem.getProfileResource()).map(resource -> (int) resource.price)
                .orElse(Optional.ofNullable(item.shopItem.cost).map(Price::getCurrentConst).orElse(0));

        h.countOfItem.setText(String.valueOf(count));
        h.costPerOne.setText(ConfigUtils.formatCurrency(cost));
        h.costPerAll.setText(ConfigUtils.formatCurrency(count * cost));
        h.title.setText(item.shopItem.title);
        item.shopItem.getIcon().ifPresent(graphics -> h.icon.setUrl(graphics.getUrl()));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.fragment_success_order_item, parent, false));
    }

    public class Holder extends ClickableHolder {
        public final TextView costPerOne;
        public final TextView costPerAll;
        public final TextView countOfItem;
        public final WebImageView icon;
        public final TextView title;


        public Holder(View itemView) {
            super(itemView);
            countOfItem = (TextView) itemView.findViewById(R.id.item_success_order_count);
            costPerAll = (TextView) itemView.findViewById(R.id.fragment_success_order_item_total_cost);
            costPerOne = (TextView) itemView.findViewById(R.id.fragment_success_order_item_cost);
            icon = (WebImageView) itemView.findViewById(R.id.fragment_success_order_item_icon);
            title = (TextView) itemView.findViewById(R.id.fragment_success_order_item_title);
        }
    }

    @Override
    protected void onItemClick(CartItem productId, int pos) {
        if (listener != null)
            listener.onClick(productId);
    }

}
