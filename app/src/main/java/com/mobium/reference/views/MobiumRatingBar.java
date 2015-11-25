package com.mobium.reference.views;

import android.content.Context;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobium.reference.R;
import com.mobium.reference.view.IRatingBar;

/**
 *  on 08.10.15.
 *
 *  Знаю, это жесть, нужно было делать что-то простое и работающиее, можете переписать унаследовав от View
 */
public class MobiumRatingBar extends LinearLayout implements IRatingBar {
    private int value = 5;
    private final int maxValue = 5;


    public MobiumRatingBar(Context context) {
        super(context);
    }

    public MobiumRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MobiumRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    public void setUp() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        ImageView star;

        for (int select = 0; select < value; select ++) {
            star = (ImageView) inflater.inflate(R.layout.view_start_select, this, false);

            addView(star);
        }

        for (int select = value; select < maxValue; select ++) {
            star = (ImageView) inflater.inflate(R.layout.view_start_select, this, false);
            star.setImageResource(R.drawable.star_margin);
            addView(star);
        }


    }


    @Override
    public void setValue(@IntRange(from = 0, to = 5) int value) {
        this.value = value;
        removeAllViews();
        setUp();
    }

    @Override
    public void setInvisible() {
        setVisibility(GONE);
    }
}
