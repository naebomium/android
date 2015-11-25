package com.mobium.reference.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import com.mobium.reference.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 06.07.15.
 * http://mobiumapps.com/
 */
public abstract class ListClickableAdapter<VH extends ClickableHolder, ITEM>
        extends ClickableAdapter<VH, ITEM> {

    protected List<ITEM> items;
    protected LayoutInflater inflater;


    public ListClickableAdapter(List<ITEM> items, Context context) {
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }

    @Override
    protected ITEM getItem(int position) {
        return items.get(position);
    }

    protected abstract void applyItemToHolder(VH holder, ITEM item, int itemPosition);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, position);
        applyItemToHolder(holder, items.get(position), position);

        if (getFirstElemMarginInDp() != 0) {
            if (position == 0)
                ((RecyclerView.LayoutParams) holder.itemView.getLayoutParams())
                        .topMargin = ImageUtils.convertToPx(holder.clickArea.getContext(), getFirstElemMarginInDp());
            else {
                ((RecyclerView.LayoutParams) holder.itemView.getLayoutParams())
                        .topMargin = 0;
            }
        }
    }

    public int getFirstElemMarginInDp() {
        return 4;
    }

    public void setItemsClearingOld(@NonNull List<ITEM> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setItemsReplacingOld(@NonNull List<ITEM> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    public void setCopyOfItems(@NonNull List<ITEM> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

}
