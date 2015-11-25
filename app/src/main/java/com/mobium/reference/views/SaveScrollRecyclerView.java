package com.mobium.reference.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 *  on 26.08.15.
 */
public class SaveScrollRecyclerView extends RecyclerView {

    private static final String TAG = "CustomRecyclerView";

    public SaveScrollRecyclerView(android.content.Context context) {
        super(context);
    }

    public SaveScrollRecyclerView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public SaveScrollRecyclerView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void scrollTo(int x, int y) {
        Log.e(TAG, "CustomRecyclerView does not support scrolling to an absolute position.");
        // Either don't call super here or call just for some phones, or try catch it. From default implementation we have removed the Runtime Exception trown
    }
}