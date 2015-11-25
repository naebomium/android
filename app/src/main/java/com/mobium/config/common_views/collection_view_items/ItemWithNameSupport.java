package com.mobium.config.common_views.collection_view_items;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobium.config.block_models.CollectionViewModel;
import com.mobium.config.common.ImageUtil;
import com.mobium.config.common_views.CollectionViewItemSupport;
import com.mobium.config.models.ItemWithName;
import com.mobium.reference.R;

/**
 *  on 05.11.15.
 */
public class ItemWithNameSupport {

    // ViewHolder for item with name
    public static class ItemWithNameVH extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;

        public ItemWithNameVH(View baseView, ImageView icon, TextView name) {
            super(baseView);
            this.icon = icon;
            this.name = name;
        }
    }

    /**
     * Build CollectionViewItemSupport for item with name
     *
     * @return interface which can return and configure viewHolders of items with name
     */

    public static CollectionViewItemSupport<ItemWithName, ItemWithNameVH> get(CollectionViewModel model) {

        /**
         * create View of item with cost
         */
        CollectionViewItemSupport.ViewCreator creator = (context, group) ->
                LayoutInflater.from(context).inflate(R.layout.category_item_card_fixed_size, group, false);

        /**
         * create binder for item with cost
         */
        CollectionViewItemSupport.Binder<ItemWithName, ItemWithNameVH> binder =
                new CollectionViewItemSupport.Binder<ItemWithName, ItemWithNameVH>() {

                    @Override
                    public void configureVH(ItemWithNameVH holder, ItemWithName item) {
                        ImageUtil.loadImage(holder.icon, item.url());
                        holder.name.setText(item.name());

                        holder.name.setTextColor(model.getColors().getTextColor(Color.BLACK));
                        holder.itemView.setBackgroundColor(model.getColors().getItemBgColor(Color.WHITE));
                    }

                    @Override
                    public ItemWithNameVH create(View view) {
                        return new ItemWithNameVH(view,
                                (ImageView) view.findViewById(R.id.category_item_fixed_size_image),
                                (TextView) view.findViewById(R.id.category_item_fixed_size_title)
                        );
                    }
                };
        return new CollectionViewItemSupport<>(binder, creator);
    }
}
