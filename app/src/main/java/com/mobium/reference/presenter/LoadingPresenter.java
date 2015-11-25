package com.mobium.reference.presenter;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
public interface LoadingPresenter {
    void startLoadingIfNeed();
    void stopLoading();

    void initializeNotLoadedViews();
}
