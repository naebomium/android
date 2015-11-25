package com.mobium.reference.views.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ShopItem> sales;
    private final int leftMargin;
    private final Ui_configurator configurator;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public View clickView;
        public WebImageView icon;
        public TextView title;
        public TextView cost;
        public TextView oldCost;

        public ViewHolder(View v) {
            super(v);
            clickView = v;
            icon = (WebImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.label);
            cost = (TextView) v.findViewById(R.id.cost);
            oldCost = (TextView) v.findViewById(R.id.oldCost);
        }
    }

    public ProductAdapter(FragmentActivity activity, Ui_configurator configurator, List<ShopItem> sales) {
        this.sales = sales;
        leftMargin = ImageUtils.convertToPx(activity, 2);
        this.configurator = configurator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item_card_fixed_size, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShopItem sale = sales.get(position);


        configurator.configureShopItemInfo(
                sale, null, holder.title, holder.cost, holder.oldCost, null, null
        );

        configurator.configureOnShopItemClicks(
                holder.clickView, sale
        );

        sale.getIcon().ifPresent(graphics ->
                        Picasso.with(holder.icon.getContext())
                                .load(graphics.getUrl())
                                .into(holder.icon)
        );

//        ViewGroup.LayoutParams params = holder.clickView.getLayoutParams();
//        if (params instanceof ViewGroup.MarginLayoutParams)
//            ((ViewGroup.MarginLayoutParams) params).leftMargin =
//                    position == 0 ? 0 : leftMargin;

    }

    @Override
    public int getItemCount() {
        return sales.size();
    }
}
