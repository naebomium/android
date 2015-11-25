package com.mobium.reference.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mobium.reference.R;


/**
 *  on 13.07.15.
 * http://mobiumapps.com/
 */
public class TitledTextController extends FrameLayout {
    public TextView title;
    public TextView text;


    public TitledTextController(Context context) {
        super(context);
        setUpViews();
    }



    public TitledTextController(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpViews();
    }

    public TitledTextController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpViews();
    }


    public TextView getTitle() {
        return title;
    }

    public TextView getText() {
        return text;
    }

    private void setUpViews() {
        View rootView = View.inflate(getContext(), R.layout.view_titled_text, this);
        title = (TextView) rootView.findViewById(R.id.TitledText_title);
        text = (TextView) rootView.findViewById(R.id.TitledText_text);
    }

    public void init(String title, String text) {
        this.title.setText(title);
        this.text.setText(text);
    }
}
