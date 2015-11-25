package com.mobium.reference.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 *  on 17.11.14.
 * ViewPager для пролистывания фотографий, загружаемых из сети
 */


public class ZoomImageViewPager extends SaveTouchViewPager {

    private final ZoomImageAdapter adapter = new ZoomImageAdapter();

    //возращает i-ой фотографии исходный размер
    public void setZoomDefault(int i) {
        WebTouchImageView photo = getWebTouchImageView(i);
        if (photo != null) {
            photo.disabledZoom();
        }
    }


    //возращает i-ую фотографию, если она найдена, иначе null
    public WebTouchImageView getWebTouchImageView(int i) {
        if (i >= getChildCount() && i < 0) {
            Log.e("incorrect i", String.valueOf(i));
            return null;
        }


        View view = findViewWithTag("Photo position =" + String.valueOf(i));

        if (view instanceof WebTouchImageView)
            return (WebTouchImageView) view;

        Log.e(" NotFound", String.valueOf(i));
        return null;

    }


    class ZoomImageAdapter extends PagerAdapter {
        private String[] photoUrls;
        private Drawable waitingImage;

        @Override
        public int getCount() {
            return photoUrls == null ? 0 : photoUrls.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final WebTouchImageView photo = new WebTouchImageView(container.getContext());

            container.addView(photo, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            photo.setUrl(photoUrls[position]);
            photo.setTag("Photo position =" + String.valueOf(position));

            return photo;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void setPhotoUrls(String... urls) {
            this.photoUrls = urls;
        }

        public void setWaitingImage(Drawable waitingImage) {
            this.waitingImage = waitingImage;
        }
    }


    public ZoomImageViewPager(Context context) {
        this(context, null);
    }


    public ZoomImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOffscreenPageLimit(3);
        setAdapter(adapter);
    }

    public void setPhotoUrls(String... urls) {
        adapter.setPhotoUrls(urls);
        adapter.notifyDataSetChanged();
    }

    public void setWaitingDrawable(Drawable waitingDrawable) {
        adapter.setWaitingImage(waitingDrawable);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onDragEvent(DragEvent event) {
        try {
            return super.onDragEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
