package com.mobium.config.common;
import android.view.View;

/**
 *  on 30.10.15.
 */
public interface CanSearch {
    void setSearchHandler(Handler<String> queryHandler);
    void setOnVoiceSearchListneter(View.OnClickListener listener);
    void setOnScannerClickListener(View.OnClickListener listener);
}
