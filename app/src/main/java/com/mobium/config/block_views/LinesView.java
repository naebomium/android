package com.mobium.config.block_views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobium.client.models.Action;
import com.mobium.config.block_models.LinesModel;
import com.mobium.config.common.Handler;
import com.mobium.config.common.HaveActions;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 02.11.2015.
 */
public class LinesView extends BaseView<LinesModel> implements HaveActions {

    LinearLayout layout;
    private Handler<Action> handler;
    private List<ButtonView> buttonViews = new ArrayList<>();

    public LinesView(LinesModel model) {
        super(model);
    }

    @Override
    protected View buildView(Context context, @NonNull ViewGroup viewGroup, boolean add) {
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(getDefaultParams());

        for (LinesModel.Line line : model.getLines()) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(getDefaultParams());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(linearLayout);

            for (LinesModel.LineItem elem : line.getLineElements()) {
                ButtonView v = new ButtonView(elem.getButton(), elem.getWeight());
                v.setActionHandler(handler);
                buttonViews.add(v);
                v.getView(context, linearLayout, true);
            }
        }

        if (add)
            viewGroup.addView(layout);

        return layout;
    }

    @Override
    public void setActionHandler(Handler<Action> handler) {
        this.handler = handler;
        for (ButtonView buttonView : buttonViews)
            buttonView.setActionHandler(handler);
    }


}
