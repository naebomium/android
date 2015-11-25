package com.mobium.reference.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.annimon.stream.Stream;
import com.mobium.reference.R;

import java.util.ArrayList;
import java.util.List;

import static com.mobium.reference.utils.ImageUtils.convertToPx;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 */
public class DotedViewPager extends LinearLayout {
    protected ViewPager pager;
    protected LinearLayout dotLayout;
    private List<DotView> dots;
    private Context context;
    private ImageView backgroud;
    private onPageChangeListener listener;
    private ProgressBar progressBar;

    protected @ColorRes int  activeColor;
    protected @ColorRes int notActiveColor;


    public DotedViewPager(Context context) {
        super(context);
        setUpUi(context);
    }


    public DotedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpUi(context);
        parseAtr(attrs);
    }



    public DotedViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpUi(context);
        parseAtr(attrs);
    }


    private void parseAtr(AttributeSet attrs) {
        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DotedViewPager);

        Stream.ofRange(0, a.length())
                .map(a::getIndex)
                .forEach(integer -> setAtr(a, integer));
        a.recycle();
    }

    private void setAtr(TypedArray a, Integer atrId) {
        switch (atrId) {
            case R.styleable.DotedViewPager_background_image_res:
                backgroud.setImageResource(a.getResourceId(atrId, 0));
                backgroud.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
        }
    }

    private void setUpUi(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_doted_view_pager, this, true);
        pager = (ViewPager) view.findViewById(R.id.view_doted_view_pager_pager);
        dotLayout = (LinearLayout) view.findViewById(R.id.view_doted_view_pager_dots);
        backgroud = (ImageView) view.findViewById(R.id.view_doted_view_pager_image);
        progressBar = (ProgressBar) view.findViewById(R.id.view_doted_view_pager_progress);
    }

    public void setAdapter(PagerAdapter adapter) {
        pager.setAdapter(adapter);


        int size = adapter.getCount();
        dots = new ArrayList<>(size);
        dotLayout.removeAllViews();

        if (size < 2)
            return;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                convertToPx(context, 8),
                convertToPx(context, 8)
        );

        lp.setMargins(convertToPx(context, 2), 0, convertToPx(context, 2), 0);

        for (int i = 0; i < size; i++) {
            DotView view = new DotView(context);

            view.setActiveColor(activeColor);
            view.setNotActiveColor(notActiveColor);

            dots.add(i, view);
            dotLayout.addView(view, i, lp);
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (DotView dotView : dots)
                    dotView.setState(DotView.State.NOT_ACTIVE);

                dots.get(i).setState(DotView.State.ACTIVE);
                if (listener != null)
                    listener.onPageSelect(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        Stream.of(dots).forEach(dotView -> dotView.setState(DotView.State.NOT_ACTIVE));
        dots.get(0).setState(DotView.State.ACTIVE);
        pager.setCurrentItem(0);
    }

    public ImageView getBackgroundImageView() {
        return backgroud;
    }


    public void setListener(onPageChangeListener listener) {
        this.listener = listener;
    }

    public interface onPageChangeListener{
        void onPageSelect(int i);
    }


    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
    }

    public void setNotActiveColor(int notActiveColor) {
        this.notActiveColor = notActiveColor;
    }

    public void setCurrent(int current) {
        if (pager.getAdapter() != null && current < pager.getAdapter().getCount())
            pager.setCurrentItem(current, false);
    }

    public int getCurrent() {
        return pager.getCurrentItem();
    }

    public ViewPager getPager() {
        return pager;
    }

    public void showProgress() {
        progressBar.setVisibility(VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(GONE);
    }

    public void setProgressBarColor(int color) {
        progressBar.getIndeterminateDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
