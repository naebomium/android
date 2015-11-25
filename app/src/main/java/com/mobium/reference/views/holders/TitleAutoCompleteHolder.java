package com.mobium.reference.views.holders;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobium.reference.R;
import com.mobium.reference.views.DelayAutoCompleteTextView;

/**
 *  on 22.10.15.
 */
public class TitleAutoCompleteHolder {
    public final TextView title;
    public final DelayAutoCompleteTextView autoCompleteTextView;
    public final ProgressBar progressBar;

    public TitleAutoCompleteHolder(TextView title, DelayAutoCompleteTextView autoCompleteTextView, ProgressBar progressBar) {
        this.title = title;
        this.autoCompleteTextView = autoCompleteTextView;
        this.progressBar = progressBar;
    }

    public static TitleAutoCompleteHolder bind(View view) {
        return new TitleAutoCompleteHolder(
                (TextView) view.findViewById(R.id.view_title),
                (DelayAutoCompleteTextView) view.findViewById(R.id.autoText),
                (ProgressBar) view.findViewById(R.id.progress)
        );
    }

}
