package com.mobium.config.block_views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.mobium.config.block_models.BlockModel;
import com.mobium.config.block_models.Inset;
import com.mobium.config.block_models.Insets;
import com.mobium.config.block_models.StickyType;
import com.mobium.reference.views.StickyScrollView;

/**
 *  on 29.10.15.
 */
public abstract class BaseView <Model extends BlockModel> {
    private static final String stikytag = StickyScrollView.STICKY_TAG;
    protected final Model model;

    public BaseView(Model model) {
        this.model = model;
    }

    public int getNone() {
        return 0;
    }
    public int getSmall() {
        return 8;
    }
    public int getNormal() {
        return 16;
    }
    public int getLarge() {
        return 24;
    }

    protected final ViewGroup.MarginLayoutParams getDefaultParams() {
        return new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    protected abstract View buildView(Context context, @NonNull ViewGroup viewGroup, boolean add);

    @NonNull
    public View getView(Context context, @NonNull ViewGroup viewGroup, boolean add) {
        View result = buildView(context, viewGroup, add);
        int none = convertDpToPx(context, getNone());
        int small = convertDpToPx(context, getSmall());
        int normal = convertDpToPx(context, getNormal());
        int large = convertDpToPx(context, getLarge());

        Insets padding = model.getPadding();

        result.setPadding(
                findMargin(padding.left, none, small, normal, large),
                findMargin(padding.top, none, small, normal, large),
                findMargin(padding.right, none, small, normal, large),
                findMargin(padding.bottom, none, small, normal, large)
        );

        if (result.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params;
            params = (ViewGroup.MarginLayoutParams) result.getLayoutParams();

            Insets margin = model.getMargin();

            params.bottomMargin += findMargin(margin.bottom, none, small, normal, large);
            params.leftMargin += findMargin(margin.left, none, small, normal, large);
            params.rightMargin += findMargin(margin.right, none, small, normal, large);
            params.topMargin += findMargin(margin.top, none, small, normal, large);

            result.setLayoutParams(params);
        }

        if (model.getStickyType().equals(StickyType.SCROLL_TOP))
            result.setTag(stikytag);
        return result;
    }


    public static int findMargin(Inset inset, int none, int small, int normal, int large) {
        if (inset == null)
            return none;
        switch (inset) {
            case LARGE:
                return large;
            case NONE:
                return none;
            case SMALL:
                return small;
            case NORMAL:
                return normal;
            default:
                return none;
        }
    }


    public static int convertDpToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    public Model getModel() {
        return model;
    }
}
