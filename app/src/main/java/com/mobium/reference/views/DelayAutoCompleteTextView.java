package com.mobium.reference.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

/**
 *  on 28.08.15.
 */
public class DelayAutoCompleteTextView extends AutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;
    private int threshold;
    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;

    private Runnable run;

    private ProgressBar mLoadingIndicator;

    private Handler handler = new Handler();


    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLoadingIndicator(ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        threshold = this.getThreshold();

        // perform filter on null to hide dropdown
        doFiltering(null, keyCode);

        // stop execution of previous handler
        handler.removeCallbacks(run);

        // creation of new runnable and prevent filtering of texts which length
        // does not meet threshold
        run = () -> {
            if (text.length() > threshold) {
                doFiltering(text, keyCode);
            }
        };

        // restart handler
        handler.postDelayed(run, mAutoCompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }

    private void doFiltering(CharSequence text, int keyCode) {
        super.performFiltering(text, keyCode);
    }


}
