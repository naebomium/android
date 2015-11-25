package com.mobium.reference.views;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.annimon.stream.function.Supplier;
import com.mobium.reference.R;


/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class VisibilityViewUtils {

    public static void show(View view, boolean animated) {
        if (view != null) {
            if (!animated)
                view.setVisibility(View.VISIBLE);
            else {
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.abc_fade_in);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        }
    }

    public static void hide(@Nullable View view, boolean animate) {
        if (view != null) {
            if (!animate)
                view.setVisibility(View.GONE);
            else {
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.abc_fade_out);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        }
    }

    public static Animation showViewAfterTransactionAnimation(Supplier<View> viewSupplier, boolean enter, Animation animation) {
        View view = viewSupplier.get();
        if (view == null)
            return animation;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (!enter)
                    VisibilityViewUtils.hide(view, false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (enter)
                    VisibilityViewUtils.show(view, true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }
}
