package com.mobium.reference.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobium.client.models.ShopCategory;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.ViewHolder> {
    private final int leftMargin;
    private final List<ShopCategory> categories;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View clickView;
        public WebImageView icon;
        public TextView title;

        public ViewHolder(View v) {
            super(v);
            clickView = v;
            icon = (WebImageView) v.findViewById(R.id.category_item_fixed_size_image);
            title = (TextView) v.findViewById(R.id.category_item_fixed_size_title);
        }
    }


    public TopCategoryAdapter(MainDashboardActivity activity, List<ShopCategory> categories) {
        this.categories = categories;
        leftMargin = ImageUtils.convertToPx(activity, 2);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_item_card_fixed_size, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopCategory category = categories.get(position);
        holder.title.setText(category.title);
        Ui_configurator.getInstance(holder.title.getContext()).configureOnShopItemClicks(
                holder.clickView, category
        );

        category.getIcon().ifPresent(graphics ->
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
        return categories.size();
    }


}
