package com.mobium.reference.presenter;

import android.view.View;

/**
 *  on 05.10.15.
 */
public interface PreCartPresenter {
    void mayLoad();

    void abortLoad();

    void viewCreated();

    void onAddButtonClick(View view);

    void onRemoveButtonClick(View view);

    String title();

}
