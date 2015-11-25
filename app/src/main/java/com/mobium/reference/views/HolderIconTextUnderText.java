package com.mobium.reference.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mobium.reference.R;


/**
 *  on 03.08.15.
 * http://mobiumapps.com/
 */
public class HolderIconTextUnderText {
    @Bind(R.id.text)
    public TextView textView;
    @Bind(R.id.under_title)
    public TextView underText;
    @Bind(R.id.icon)
    public ImageView imageView;
    @Bind(R.id.view_icon_text_underText)
    public View clickArea;
    @Bind(R.id.deliver)
    public View deliver;

    public HolderIconTextUnderText(View view) {
        ButterKnife.bind(this, view);
    }

}
