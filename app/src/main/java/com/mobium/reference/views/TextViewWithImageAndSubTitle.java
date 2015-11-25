package com.mobium.reference.views;

import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobium.reference.R;


/**
 *  on 14.07.15.
 * http://mobiumapps.com/
 */
public class TextViewWithImageAndSubTitle {
    public final ImageView image;
    public final TextView title;
    public final TextView subTitle;

    public TextViewWithImageAndSubTitle(ImageView image, TextView title, TextView subTitle) {
        this.image = image;
        this.title = title;
        this.subTitle = subTitle;
    }

    public TextViewWithImageAndSubTitle(View view) {
        this(
            (ImageView) view.findViewById(R.id.textViewWithImage_icon),
            (TextView) view.findViewById(R.id.textViewWithImage_title),
            (TextView) view.findViewById(R.id.textViewWithImage_subtitle)
        );
    }

    public void configure(String s_title, String s_subtitle, @DrawableRes int res) {
        image.setImageResource(res);
        title.setText(s_title);
        subTitle.setText(s_subtitle);
    }

}
