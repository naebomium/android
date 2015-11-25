package com.mobium.config.common_views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mobium.client.models.Action;
import com.mobium.config.common.Handler;
import com.mobium.config.models.ItemWithCost;
import com.mobium.config.models.ItemWithName;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 05.11.15.
 */
public class OffsetAdapter<Item, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int progress = 0;
    private static final int item = 1;

    private final List<Item> items;
    private final CollectionViewItemSupport.Binder<Item, ViewHolder> binder;
    private final CollectionViewItemSupport.ViewCreator viewCreator;
    private final Handler<Action> actionHandler;

    private boolean showProgressBar;


    public OffsetAdapter(List<Item> items, CollectionViewItemSupport<Item, ViewHolder> collectionViewItemSupport, Handler<Action> actionHandler) {
        this.actionHandler = actionHandler;
        viewCreator = collectionViewItemSupport.creator;
        binder = collectionViewItemSupport.binder;
        showProgressBar = false;
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case progress:
                return new ProgressViewHolder(new ProgressBar(parent.getContext()));
            case item:
                return binder.create(viewCreator.view(parent.getContext(), parent));
            default:
                throw new IllegalArgumentException("wrong viewType");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (items.size() > position) {
            Item o = items.get(position);
            binder.configureVH((ViewHolder) holder, o);
            holder.itemView.setOnClickListener(v -> {
                Action action = null;
                if (o instanceof ItemWithCost)
                    action = ((ItemWithCost)o).onClick();
                else if (o instanceof ItemWithName)
                    action = ((ItemWithName) o).onClick();
                if (action != null)
                    actionHandler.onData(action);
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() > position)
            return item;
        return progress;
    }

    @Override
    public int getItemCount() {
        return items.size() + (showProgressBar ? 1 : 0);
    }

    public void addData(List<Item> data, int offset) {
        int currentSize = items.size();
        items.addAll(data);
        showProgressBar = false;
        notifyItemRangeInserted(currentSize, data.size());
    }

    protected class ProgressViewHolder extends RecyclerView.ViewHolder{
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    public boolean isShowProgressBar() {
        return showProgressBar;
    }

    public void waitForData() {
        this.showProgressBar = true;

    }
}
