package com.mobium.config.common_views.collection_view_items;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobium.config.block_models.CollectionViewModel;
import com.mobium.config.common.ImageUtil;
import com.mobium.config.common_views.CollectionViewItemSupport;
import com.mobium.config.models.ItemWithCost;
import com.mobium.reference.R;

/**
 *  on 05.11.15.
 */
public class ItemWithCostItemSupport {

    // ViewHolder for item with cost
    public static class ItemWithCostVH extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView title;
        private TextView cost;
        private TextView oldCost;

        public ItemWithCostVH(View baseView, ImageView icon, TextView title, TextView cost, TextView oldCost) {
            super(baseView);
            this.icon = icon;
            this.title = title;
            this.cost = cost;
            this.oldCost = oldCost;
        }
    }

    /**
     * Build CollectionViewItemSupport for item with cost
     * @return interface which can return and configure viewHolders of items with cost
     */
    public static CollectionViewItemSupport<ItemWithCost, ItemWithCostVH> get(CollectionViewModel model) {

        /**
         * create View of item with cost
         */
        CollectionViewItemSupport.ViewCreator creator = (context, group) ->
                LayoutInflater.from(context).inflate(R.layout.shop_item_card_fixed_size, group, false);

        /**
         * create binder for item with cost
         */
        CollectionViewItemSupport.Binder<ItemWithCost, ItemWithCostVH> binder =
                new CollectionViewItemSupport.Binder<ItemWithCost, ItemWithCostVH>() {
                    @Override
                    public void configureVH(ItemWithCostVH holder, ItemWithCost item) {
                        holder.cost.setText(item.cost());
                        ImageUtil.loadImage(holder.icon, item.iconUrl());
                        holder.title.setText(item.name());
                        String oldCost = item.oldCost();
                        if (oldCost != null) {
                            holder.oldCost.setText(oldCost);
                            holder.oldCost.setVisibility(View.VISIBLE);
                        } else {
                            holder.oldCost.setVisibility(View.GONE);
                        }

                        holder.cost.setTextColor(model.getColors().getPriceColor(Color.BLACK));
                        holder.oldCost.setTextColor(model.getColors().getOldPriceColor(Color.GRAY));
                        holder.oldCost.setPaintFlags(holder.oldCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        holder.title.setTextColor(model.getColors().getTextColor(Color.BLACK));
                        holder.itemView.setBackgroundColor(model.getColors().getItemBgColor(Color.WHITE));
                    }

                    @Override
                    public ItemWithCostVH create(View view) {
                        return new ItemWithCostVH(view,
                                (ImageView) view.findViewById(R.id.image),
                                (TextView) view.findViewById(R.id.label),
                                (TextView) view.findViewById(R.id.cost),
                                (TextView) view.findViewById(R.id.oldCost)
                        );
                    }
                };

        return new CollectionViewItemSupport<>(binder, creator);
    }
}
