package com.mobium.reference.views;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
public class ViewUtils {
    public static void setRatioOfView(@NonNull View view, double ratio) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = view.getMeasuredWidth();
                view.getLayoutParams().height = (int) (width * ratio);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
