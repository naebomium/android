package com.mobium.reference.views.holders;

import android.view.View;
import android.widget.TextView;

import com.mobium.reference.R;

/**
 *  on 22.10.15.
 */
public class TitleTextHolder {
    public final TextView text;
    public final TextView title;

    public TitleTextHolder(TextView text, TextView title) {
        this.text = text;
        this.title = title;
    }
    public static TitleTextHolder bind(View v) {
        return new TitleTextHolder((TextView)v.findViewById(R.id.view_text), (TextView)v.findViewById(R.id.view_title));
    }
}
