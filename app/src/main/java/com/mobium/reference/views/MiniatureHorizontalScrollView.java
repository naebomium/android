package com.mobium.reference.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

/**
 *  on 25.12.13.
 * http://mobiumapps.com/
 */
public class MiniatureHorizontalScrollView extends HorizontalScrollView {

    public MiniatureHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void scrollToItem(int index) {
        int scrollTo = getItemPosition(index);
        smoothScrollTo(scrollTo, 0);
    }

    private int getItemPosition(int index) {
        ViewGroup vg = (ViewGroup) getChildAt(0);
        if (index > 0) {
            View v = vg.getChildAt(index - 1);
            if(v == null)
                return 0;
            else return v.getLeft();
        } else {
            return 0;
        }
    }

    public int getSelectedItemIndex() {
        int scrollX = getScrollX();
        int itemWidth = getMeasuredWidth();
        return (scrollX + itemWidth / 2) / itemWidth;
    }
}
