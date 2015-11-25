package com.mobium.reference.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.mobium.reference.R;


/**
 *   25.03.15.
 * viewPager, методы нажатия которого обернуты в блок try
 * square - если true, pager займет квадрат со стороной width
 */
public class SaveTouchViewPager extends ViewPager {
    private boolean mIsDisallowIntercept = false;

    private boolean square;
    private boolean _locked = false;

    public SaveTouchViewPager(Context context) {
        super(context);

    }

    public SaveTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SaveTouchViewPager);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SaveTouchViewPager_square) {
                setSquare(a.getBoolean(attr, false));
            }

        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.i(SaveTouchViewPager.class.getSimpleName(), "In requestDisallowInterceptTouchEvent!!!");
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if ( _locked ) {
            return false;
        } else {
            try{
                return super.onInterceptTouchEvent(ev);
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (square)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    /**
     * Изменяет пропорции вью на квадрат.(Полезно для листинга фотогафий)
     * @param square если 1, высота вью станет равна его ширине
     */
    public void setSquare(boolean square) {
        if (square == this.square)
            return;
        this.square = square;
        invalidate();
    }
}
