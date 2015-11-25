package com.mobium.reference.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 *  on 10.04.15.
 * растягивается по высоте в соотвествии с наполнением.
 */



public class WrapContentRealHeightGridView  extends GridView {
    public WrapContentRealHeightGridView(Context context) {
        super(context);
    }

    public WrapContentRealHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentRealHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST));
        getLayoutParams().height = getMeasuredHeight();
    }
}
