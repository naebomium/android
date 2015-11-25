/*
 * Copyright (c) 2013 Extradea LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobium.reference.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.annimon.stream.Objects;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.squareup.picasso.*;

public class WebImageView extends ImageView {
    private static Drawable errorImage;

    private MemoryPolicy memoryPolicy;
    private NetworkPolicy networkPolicy;

    private OnImageLoadedListener onImageLoadedListener;
    private static Handler handler;
    private static Picasso picasso;

    private boolean squareWidth = false;

    public WebImageView(Context context) {
        super(context);
        configure(context, null);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        configure(context, attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configure(context, attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        configure(context, attrs);
    }

    private void configure(Context c, AttributeSet attrs) {
        if (picasso == null) {
            handler = new Handler(c.getMainLooper());
            picasso = new Picasso.Builder(c.getApplicationContext())
                    .downloader(new OkHttpDownloader(ReferenceApplication.getInstance().okHttpClient))
                    .build();
        }
        if (attrs != null) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.WebImageView);
            for (int i = 0; i < a.getIndexCount(); ++i) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.WebImageView_squareWidth) {
                    squareWidth = a.getBoolean(attr, false);
                }
            }
            a.recycle();
        }
    }


    public static void setErrorImage(Drawable image) {
        errorImage = image;
    }



    private RequestCreator getCreator( String url) {
        RequestCreator result = picasso
                .load(url)
                .tag(getContext());

        return (errorImage != null) ? result.error(errorImage) : result;
    }



    public final void setUrl(String url) {
        final RequestCreator creator = getCreator(url);

        if (networkPolicy != null)
            creator.networkPolicy(networkPolicy);

        handler.post(() ->
            creator.into(this, new Callback() {
                @Override
                public void onSuccess() {
                    onLoadSuccess();
                }

                @Override
                public void onError() {
                        setImageDrawable(errorImage);
                        onLoadError();
                   }
            })
        );
    }

    public interface OnImageLoadedListener {
        void onImageLoaded();
    }

    public void setOnImageLoadedListener(OnImageLoadedListener onImageLoadedListener) {
        this.onImageLoadedListener = onImageLoadedListener;
    }

    protected void onLoadSuccess() {
        if (this.onImageLoadedListener != null)
            onImageLoadedListener.onImageLoaded();
    }

    protected void onLoadError() {

    }

    public void setMemoryPolicy(MemoryPolicy memoryPolicy) {
        this.memoryPolicy = memoryPolicy;
    }

    public void setNetworkPolicy(NetworkPolicy networkPolicy) {
        this.networkPolicy = networkPolicy;
    }

    public static class StopLoadScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                picasso.resumeTag(recyclerView.getContext());
            } else {
                picasso.pauseTag(recyclerView.getContext());
            }
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!squareWidth)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
}
