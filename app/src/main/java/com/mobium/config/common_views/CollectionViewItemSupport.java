package com.mobium.config.common_views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  on 05.11.15.
 */
public class CollectionViewItemSupport<Item, ViewHolder extends RecyclerView.ViewHolder> {
    public final Binder<Item, ViewHolder> binder;
    public final ViewCreator creator;


    public CollectionViewItemSupport(Binder<Item, ViewHolder> binder, ViewCreator creator) {
        this.binder = binder;
        this.creator = creator;
    }

    public interface Binder<I, VH extends RecyclerView.ViewHolder> {
        void configureVH(VH holder, I item);
        VH create(View view);
    }

    public interface ViewCreator {
        View view(Context context, ViewGroup group);
    }

    static class ItemWithIcon extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView title;
        public ItemWithIcon(View itemView) {
            super(itemView);
        }

        public ItemWithIcon(View baseView, ImageView icon, TextView textView) {
            super(baseView);
            this.icon = icon;
            this.title = textView;
        }
    }


}
