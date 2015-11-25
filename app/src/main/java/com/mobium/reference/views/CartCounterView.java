package com.mobium.reference.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mobium.reference.R;


/**
 *   28.02.15.
 * http://mobiumapps.com/
 */
public class CartCounterView extends FrameLayout {
    TextView counterView;

    public CartCounterView(Context context) {
        super(context);
        initView(context);
    }

    public CartCounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CartCounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        View cartLayout = inflater.inflate(R.layout.view_cart_counter, this);
        counterView = (TextView) cartLayout.findViewById(R.id.cartCounter);
    }


    public void updateCounter(String counterValue) {
        if (counterValue == null || counterValue.isEmpty() || "0".equals(counterValue)) {
            counterView.setVisibility(View.INVISIBLE);
        } else {
            counterView.setVisibility(View.VISIBLE);
        }
        counterView.setText(counterValue);
    }
}
