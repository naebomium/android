package com.mobium.reference.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobium.reference.R;
import com.mobium.reference.anotations.NeedInternetAccess;


/**
 *  on 24.05.15.
 * http://mobiumapps.com/
 */

@NeedInternetAccess
public abstract class AbstractLoadBarFragment extends BasicLoadableFragment {
    private View progressBar;
    private View contentView;

    protected abstract View addContentView(LayoutInflater inflater, ViewGroup contentWrapper);

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_load, container, false);
        progressBar =  root.findViewById(R.id.progress_view);
        contentView =  root.findViewById(R.id.content_wrapper);
        addContentView(inflater, (ViewGroup) contentView);
        return root;
    }


    @Override
    protected final View getContentView() {
        return contentView;
    }

    @Override
    protected final View getProgressView() {
        return progressBar;
    }
}

