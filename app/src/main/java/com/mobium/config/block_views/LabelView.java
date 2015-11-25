package com.mobium.config.block_views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobium.config.Util;
import com.mobium.config.block_models.Inset;
import com.mobium.config.block_models.LabelModel;

/**
 *  on 30.10.15.
 */
public class LabelView extends BaseView<LabelModel> {
    private TextView textView;


    public LabelView(LabelModel model) {
        super(model);
    }


    @Override
    protected View buildView(Context context, @NonNull ViewGroup viewGroup, boolean add) {
        textView = new TextView(context);
        textView.setText(model.getText());
        textView.setTextColor(model.getTextColor(Color.BLACK));
        textView.setBackgroundColor(model.getBackgroundColor(0));
        if (model.getMaxLines() > 0)
            textView.setMaxLines(model.getMaxLines());

        textView.setGravity(Util.getGravityByAlignment(model.getAlignment()));

        textView.setLayoutParams(getDefaultParams());
        if (add)
            viewGroup.addView(textView);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getFontSize(model.getSize()));
        return textView;
    }

    public @Nullable TextView getTextView() {
        return textView;
    }

    public int getFontSize(Inset size) {
        switch (size) {
            case LARGE:
                return 21;
            case NORMAL:
                return 17;
           default:
                return 13;
        }
    }
}
