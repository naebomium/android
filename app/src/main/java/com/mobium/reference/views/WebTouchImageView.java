package com.mobium.reference.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 *  on 14.11.14.
 * WebImageView, дополненный функционалом библиотеки PhotoView
 * после загрузки картинки mAttacher наделяет всеми свойствами(зум, таблтаб) картинку
 */

public class WebTouchImageView extends WebImageView {
    private PhotoViewAttacher mAttacher;

    public WebTouchImageView(Context context) {
        super(context);
    }

    public WebTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebTouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //возращает imageView в исходные размер(убирает зум)
    public void disabledZoom() {
        if (mAttacher != null) {
            mAttacher.setScale(1.0f);
            mAttacher.update();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            try {
                return super.onTouchEvent(event);
            } catch (Exception e) {

            }
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttacher != null)
            mAttacher.cleanup();
    }


    @Override
    protected void onLoadSuccess() {
        super.onLoadSuccess();
        mAttacher = new PhotoViewAttacher(WebTouchImageView.this);
    }
}