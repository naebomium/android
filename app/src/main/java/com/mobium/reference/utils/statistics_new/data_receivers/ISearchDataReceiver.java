package com.mobium.reference.utils.statistics_new.data_receivers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *  on 29.09.15.
 */
public interface ISearchDataReceiver {
    void onSearch(@NonNull String query, @Nullable String openedCategoryId);
}
