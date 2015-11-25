package com.mobium.reference.fragments.support;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.views.MiniatureHorizontalScrollView;
import com.mobium.reference.views.WebImageView;
import com.mobium.reference.views.WebTouchImageView;
import com.mobium.reference.views.ZoomImageViewPager;


/**
 *
 *
 * Date: 01.12.12
 * Time: 19:55
 * фрагмент для отображения фотографий
 * imageViewPager - для скрола основных фотографий
 * miniScrollView - для скрола миниатюр
 * dotsViews - миниаюры
 */
public class ViewPhotoFragment extends BasicContentFragment {

    private static final String IMAGE_URLS_KEY = "image_urls_key";
    private static final String MINI_IMAGE_URLS_KEY = "mini_image_urls_key";
    private static final String CURRENT_IMAGE = "current";
    private String[] imageUrls;
    private String[] miniImageUrls;
    private LinearLayout bulletLayout;
    private ImageView[] dotsViews = new ImageView[0];
    private ZoomImageViewPager imageViewPager;
    private MiniatureHorizontalScrollView miniScrollView;

    private static void setNotSelectedMini(ImageView imageView) {
        imageView.setBackgroundColor(Color.WHITE);
    }

    public static ViewPhotoFragment getInstance(@NonNull String[] imageUrls, @Nullable String[] miniImageUrls, int current) {
        ViewPhotoFragment result = new ViewPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(MINI_IMAGE_URLS_KEY, miniImageUrls);
        bundle.putStringArray(IMAGE_URLS_KEY, imageUrls);
        bundle.putInt(CURRENT_IMAGE, current);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            miniImageUrls = bundle.getStringArray(MINI_IMAGE_URLS_KEY);
            imageUrls = bundle.getStringArray(IMAGE_URLS_KEY);
            if (miniImageUrls == null)
                miniImageUrls = imageUrls;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_view_images, container, false);
        bulletLayout = (LinearLayout) res.findViewById(R.id.bulletsContainer);
        miniScrollView = (MiniatureHorizontalScrollView) res.findViewById(R.id.miniature_scroll_view);
        imageViewPager = (ZoomImageViewPager) res.findViewById(R.id.paging_horizontal_scroll_view);

        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                updateBullets(i);
                miniScrollView.scrollToItem(i);
                WebTouchImageView photo = imageViewPager.getWebTouchImageView(i);
                if (photo != null) {
                    imageViewPager.setZoomDefault(i + 1);
                    imageViewPager.setZoomDefault(i - 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        imageViewPager.setWaitingDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.galary_waiting_image, null));
        imageViewPager.setPhotoUrls(imageUrls);
        return res;
    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);
        int currentImageIndex = getArguments().getInt(CURRENT_IMAGE, 0);
        miniScrollView.scrollToItem(currentImageIndex);
        imageViewPager.setCurrentItem(currentImageIndex);
        updateAllBullets();
        highlightMini(currentImageIndex);
    }

    private void updateAllBullets() {
        bulletLayout.removeAllViews();
        dotsViews = new ImageView[miniImageUrls.length];
        for (int i = 0; i < miniImageUrls.length; i++) {
            ImageView view = getMiniImage(i);
            dotsViews[i] = view;
            bulletLayout.addView(view);
        }
    }

    private void updateBullets(int currentItemIndex) {
        if (dotsViews.length != miniImageUrls.length) {
            updateAllBullets();
        }
        highlightMini(currentItemIndex);
    }

    private ImageView getMiniImage(final int index) {
        WebImageView imageView = null;
        int width = (int) (getResources().getDisplayMetrics().density * 60);
        int height = (int) (getResources().getDisplayMetrics().density * 60);
        if (imageView == null) {
            imageView = new WebImageView(getActivity());
            imageView.setUrl(miniImageUrls[index]);
        }
        imageView.setOnClickListener(view -> {
            imageViewPager.setCurrentItem(index, true);
            highlightMini(index);
        });
        int padding = (int) (getResources().getDisplayMetrics().density * 4);
        imageView.setPadding(padding, padding, padding, padding);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(padding, padding, padding, padding);
        imageView.setLayoutParams(layoutParams);

        return imageView;
    }

    private void highlightMini(int miniIndex) {
        for (int i = 0; i < miniImageUrls.length; i++)
            highlightMiniIfNeed(miniIndex, i);
    }

    private void highlightMiniIfNeed(int currentItemIndex, int i) {
        if (currentItemIndex == i)
            setSelectedMini(dotsViews[i]);
        else
            setNotSelectedMini(dotsViews[i]);

    }

    private void setSelectedMini(ImageView imageView) {
        imageView.setBackgroundColor(getResources().getColor(R.color.application_color_accent));
    }

    @Override
    protected String getTitle() {
        return "Фото товара";
    }
}
