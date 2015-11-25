package com.mobium.reference.fragments.support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;


/**
 *
 *
 * Date: 13.12.12
 * Time: 2:50
 */
public class DebugFragment extends BasicContentFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_debug, container1, false);

        return res;
    }
}
