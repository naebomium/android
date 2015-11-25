package com.mobium.reference.view;

import android.content.DialogInterface;

/**
 *  on 13.10.15.
 */
public interface CanShowErrorView {
    void showError(String title,
                   String message,
                   String applyTitle,
                   DialogInterface.OnClickListener apply,
                   String cancelTitle,
                   DialogInterface.OnClickListener cancel
    );

}
