package com.mobium.reference.views.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mobium.config.block_models.CollectionViewModel;
import com.mobium.reference.R;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.ViewHolder> {
    private final CollectionViewModel model;
    public LoadAdapter(int size, CollectionViewModel model) {
        this.size = size;
        this.model = model;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.waiting_shop_item_progress);
            cardView = (CardView) itemView.findViewById(R.id.card);
            if (model != null && cardView != null) {
                int backGround = model.getColors().getItemBgColor(Color.WHITE);
                cardView.setCardBackgroundColor(backGround);
            }
        }
    }

    private final int size;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.waiting_shop_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
