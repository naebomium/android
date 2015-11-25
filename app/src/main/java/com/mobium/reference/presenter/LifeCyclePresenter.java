package com.mobium.reference.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *  on 28.07.15.
 * http://mobiumapps.com/
 */
public interface LifeCyclePresenter {
    void onCreate(@Nullable Bundle savedState);
    void onSavedState(@NonNull Bundle out);
    void onStart();
    void onPause();
}
