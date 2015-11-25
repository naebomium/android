package com.mobium.reference.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.R;

import java.util.List;

/**
 *  on 11.07.15.
 * http://mobiumapps.com/
 */
public class ShopPointsAdapter extends ListClickableAdapter<ShopPointsAdapter.PointHolder,
        ShopPoint> {

    private OnShopPointClickListener listener;


    public ShopPointsAdapter(List<ShopPoint> shopPoints, Context context) {
        super(shopPoints, context);
    }

    public ShopPointsAdapter(List<ShopPoint> shopPoints, Context context, OnShopPointClickListener listener) {
        super(shopPoints, context);
        this.listener = listener;
    }

    @Override
    protected void applyItemToHolder(PointHolder holder, ShopPoint shopPoint, int itemPosition) {

        holder.title.setText(shopPoint.getSubway().orElse(shopPoint.getTitle()));

        holder.subTitle.setText(shopPoint.getAddress());
        if (itemPosition == getItemCount() - 1)
            holder.separator.setVisibility(View.GONE);
        else
            holder.separator.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onItemClick(ShopPoint shopPoint, int position) {
        if (listener != null)
            listener.onClick(shopPoint);
    }

    @Override
    public PointHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PointHolder(inflater.inflate(R.layout.fragment_shops_item, parent, false));
    }

    public static class PointHolder extends ClickableHolder {
        TextView title;
        TextView subTitle;
        ImageView icon;
        View separator;
        public PointHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.fragment_shops_item_title);
            subTitle = (TextView) itemView.findViewById(R.id.fragment_shops_item_subTitle);
            icon = (ImageView) itemView.findViewById(R.id.fragment_shops_item_icon);
            separator = itemView.findViewById(R.id.fragment_shops_item_deliver);
        }
    }


    public interface OnShopPointClickListener {
        void onClick(ShopPoint shopPoint);
    }

    public void setListener(OnShopPointClickListener listener) {
        this.listener = listener;
    }

}
