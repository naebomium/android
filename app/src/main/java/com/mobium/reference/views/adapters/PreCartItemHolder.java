package com.mobium.reference.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mobium.reference.R;
import com.mobium.reference.views.WebImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  on 05.10.15.
 */
public class PreCartItemHolder {
    public @Bind(R.id.fragment_precart_item_title) TextView title;
    public @Bind(R.id.fragment_precart_item_cost) TextView cost;
    public @Bind(R.id.fragment_precart_item_count) TextView count;
    public @Bind(R.id.fragment_precart_add_button) TextView add;
    public @Bind(R.id.fragment_precart_remove_button) View remove;
    public @Bind(R.id.fragment_precart_item_total_cost) TextView totalCost;


    public PreCartItemHolder(View itemView) {
        ButterKnife.bind(this, itemView);
    }
}
