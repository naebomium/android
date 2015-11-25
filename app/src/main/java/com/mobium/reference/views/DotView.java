package com.mobium.reference.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.View;


public class DotView extends View {
    public enum State{
        ACTIVE, NOT_ACTIVE
    }
    private Paint paint;
    private State state;

    private  @ColorRes int activeColor;
    private @ColorRes  int notActiveColor;

    {
        state = State.NOT_ACTIVE;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }


    public void setState(State state) {
        this.state = state;
        init();
        invalidate();
    }

    public DotView(Context context) {
        super(context);
        init();
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        switch (state) {
            case ACTIVE:
                paint.setColor(activeColor == 0 ? Color.BLACK : activeColor);
                break;
            case NOT_ACTIVE:
                paint.setColor(notActiveColor == 0 ? Color.GRAY : notActiveColor);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        float radius = x / 2.0f;
        canvas.drawCircle(x/2.f, y/2.f, radius , paint);
    }

    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
    }

    public void setNotActiveColor(int notActiveColor) {
        this.notActiveColor = notActiveColor;
    }
}
