package com.mobium.reference.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.mobium.reference.R;


/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public abstract class ClickableAdapter<VH extends ClickableHolder, ITEM>
        extends RecyclerView.Adapter<VH> {
    protected final View.OnClickListener listener = view -> {

        IndexedItem<ITEM> pair = (IndexedItem<ITEM>) view.getTag(R.id.AbstractTag);
        if (pair != null)
            onItemClick(pair.item, pair.position);
    };




    protected abstract ITEM getItem(int position);
    protected abstract void onItemClick(ITEM item, int position);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.clickArea.setTag(R.id.AbstractTag, new IndexedItem<ITEM>(getItem(position), position));
        holder.clickArea.setOnClickListener(listener);

    }

    public static class IndexedItem <T> {
        final T item;
        final int position;

        public IndexedItem(T item, int position) {
            this.item = item;
            this.position = position;
        }
    }
}
