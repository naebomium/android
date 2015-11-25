package com.mobium.reference.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.mobium.reference.utils.FragmentUtils;

import butterknife.ButterKnife;
import static com.mobium.reference.utils.FragmentUtils.disabledMenuItems;

/**
 *  on 03.08.15.
 * http://mobiumapps.com/
 */

public abstract class InjectAbstractFragment extends Fragment
        implements FragmentUtils.TitleChanger, FragmentUtils.BackButtonHandler {

    @Override
    public void onStart() {
        super.onStart();

        setHasOptionsMenu(true);

        if (getActivity() instanceof FragmentUtils.ActionBarView)
            ((FragmentUtils.ActionBarView) getActivity()).updateTitle(getTitle());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        disabledMenuItems(menu);
    }

    @Nullable
    @Override
    public final View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutRes(), container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public boolean onBackPressed() {
        return false;
    }
}
