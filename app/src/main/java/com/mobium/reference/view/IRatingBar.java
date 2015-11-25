package com.mobium.reference.view;

import android.support.annotation.IntRange;


/**
 *  on 08.10.15.
 */
public interface IRatingBar {
    void setValue(@IntRange(from = 0, to = 5) int value);
    void setInvisible();
}
