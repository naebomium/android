package com.mobium.config.block_views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mobium.client.models.Action;
import com.mobium.config.Util;
import com.mobium.config.block_models.ButtonModel;
import com.mobium.config.common.Handler;
import com.mobium.config.common.HaveActions;
import com.mobium.reference.R;

/**
 *  on 02.11.2015.
 */
public class ButtonView extends BaseView<ButtonModel> implements HaveActions {

    private Button button;
    private int weight;
    private Handler<Action> handler;

    public ButtonView(ButtonModel model, int weight) {
        super(model);
        this.weight = weight;
    }

    @Override
    protected View buildView(Context context, @NonNull ViewGroup viewGroup, boolean add) {
        button = new Button(context);
        button.setText(model.getTitle());
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = weight;
        button.setLayoutParams(layoutParams);

        button.setBackgroundColor(model.getBackgroundColor(Color.WHITE));
        button.setTextColor(model.getTitleColor(Color.BLACK));
        if (model.getImagePath() != null)
            setDrawable(context, model.getImagePath(), model.getImagePosition());
        button.setTextSize(model.getFontSize());
        if (add)
            viewGroup.addView(button);

        button.setOnClickListener(v -> {
            if (handler != null) {
                Action action = new Action(model.getActionType(), model.getData());
                handler.onData(action);
            }
        });
        button.setMinimumHeight(convertDpToPx(context, 50));
        return button;
    }

    public void setDrawable(Context context, String drawableName, ButtonModel.IMAGE_POSITION position) {
        if (position == null)
            return;

        Drawable icon = Util.drawableByName(drawableName, context);
        if (icon == null)
            return;

        switch (position) {
            case LEFT:
                button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                break;
            case TOP:
                button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                break;
            case RIGHT:
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                break;
            case BOTTOM:
                button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon);
                break;
        }
    }


    public
    @Nullable
    Button getButton() {
        return button;
    }

    @Override
    public void setActionHandler(Handler<Action> handler) {
        this.handler = handler;
    }
}
