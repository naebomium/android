package com.mobium.reference.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.mobium.reference.R;


/**
 *   31.03.15.
 * http://mobiumapps.com/
 */
public class ExpendableFrameLayout extends FrameLayout {
    private int heightCoefficient = 1;


    public ExpendableFrameLayout(Context context) {
        super(context);
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightCoefficient * heightMeasureSpec);
//    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, heightCoefficient * b);
//    }

    public ExpendableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpendableFrameLayout);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ExpendableFrameLayout_heightCoefficient) {
                heightCoefficient = a.getInt(attr, 1);
            }

        }

        final ViewTreeObserver observer= this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            @Override
            public void onGlobalLayout() {
                int height = getHeight();
                int width = getWidth();

                setLayoutParams(new LinearLayout.LayoutParams(width, heightCoefficient * height));


                if (Build.VERSION.SDK_INT < 16) {
                    ExpendableFrameLayout.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    ExpendableFrameLayout.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        a.recycle();
    }



}
