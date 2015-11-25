package com.mobium.reference.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *  on 06.07.15.
 * http://mobiumapps.com/
 */
public class ClickableHolder extends RecyclerView.ViewHolder {
    public View clickArea;

    public ClickableHolder(View itemView) {
        super(itemView);
        setClickArea(itemView);
    }

    protected void setClickArea(View itemView) {
        clickArea = itemView;
    }
}
