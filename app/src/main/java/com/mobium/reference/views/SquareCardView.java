package com.mobium.reference.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 *  on 10.04.15.
 */
public class SquareCardView extends CardView {
    public SquareCardView(Context context) {
        super(context);
    }

    public SquareCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int max = widthMeasureSpec > heightMeasureSpec ? widthMeasureSpec : heightMeasureSpec;
        super.onMeasure(max, max);
    }

    @Override
    public void setBackgroundColor(int color) {
        setCardBackgroundColor(color);
    }
}
