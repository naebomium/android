package com.mobium.reference.fragments;

import android.app.Activity;

import com.mobium.config.common.Config;
import com.mobium.config.prototype.INavigationBar;
import com.mobium.reference.R;
import com.mobium.reference.utils.FragmentUtils;

/**
 *  on 03.11.15.
 */
public class LaunchFragment extends ConfigScreen {

    @Override
    public void onStart() {
        super.onStart();
        Activity activity = getActivity();
        INavigationBar model = Config.get().design().defaultNavigationBar();

        if (activity instanceof FragmentUtils.ActionBarView) {
          ((FragmentUtils.ActionBarView) activity).configure(model);
        }
    }
}
