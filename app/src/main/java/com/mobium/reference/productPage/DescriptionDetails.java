package com.mobium.reference.productPage;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.ImageUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import static android.view.ViewTreeObserver.OnPreDrawListener;

/**
 *  on 08.04.15.
 * http://mobiumapps.com/
 */
public class DescriptionDetails extends ProductDetailsBase {
    private HtmlTextView descriptionContent;
    private boolean isSowFull;

    private final int WrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int DP150 = ImageUtils.convertToPx(activity, 150);


    private TextView showMoreButton;

    public DescriptionDetails(MainDashboardActivity context, ShopItem item) {
        super(context, DetailsType.DESCRIPTION, item);
    }


    private void fillUi() {
        descriptionContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        final int height = descriptionContent.getHeight();
                        if (height == 0 || height <= DP150)
                            return;

                        descriptionContent.getLayoutParams().height = DP150;
                        showMoreButton.setVisibility(View.VISIBLE);
                        showMoreButton.setOnClickListener(view -> {
                            isSowFull = !isSowFull;
                            showMoreButton.setText(isSowFull ? "Скрыть" : "Показать полностью");
                            descriptionContent.getLayoutParams().height = isSowFull ? WrapContent : DP150;
                            new Handler().postDelayed(() -> {
                                descriptionContent.requestLayout();
                                if (focuser != null && !isSowFull) {
                                    focuser.focusOnTopTabs();
                                }
                            }, 300);
                        });

                        descriptionContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                });


//        ViewTreeObserver viewTreeObserver = descriptionContent.getViewTreeObserver();
//        viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                final int measuredHeight = descriptionContent.getMeasuredHeight();
//                if (measuredHeight == 0 || measuredHeight <= DP150)
//                    return false;
//
//                descriptionContent.getLayoutParams().height = DP150;
//
//                showMoreButton.setVisibility(View.VISIBLE);
//                showMoreButton.setOnClickListener(view -> {
//                    isSowFull = !isSowFull;
//                    showMoreButton.setText(isSowFull ? "Скрыть" : "Показать полностью");
//                    descriptionContent.getLayoutParams().height = isSowFull ? WrapContent : DP150;
//                    new Handler().postDelayed(() -> {
//                        descriptionContent.requestLayout();
//                        if (focuser != null && !isSowFull) {
//                            focuser.focusOnTopTabs();
//                        }
//                    }, 300);
//                });
//                descriptionContent.getViewTreeObserver().removeOnPreDrawListener(this);
//                return false;
//            }
//        });

        setText(shopItem.getDescription().get());
    }


    @Override
    protected View fillContentWrapper(final LayoutInflater inflater, ViewGroup contentWrapper) {

        final View result = inflater.inflate(R.layout.fragment_product_detailds_description, contentWrapper, true);
        descriptionContent = (HtmlTextView) result.findViewById(R.id.fragment_product_description_text);

        showMoreButton = (TextView) result.findViewById(R.id.show_more);

        descriptionContent.setOnTouchListener((v, event) -> (event.getAction() == MotionEvent.ACTION_MOVE));

        fillUi();
        return result;
    }


    private void setText(String text) {
        descriptionContent.setHtmlFromString(text, new HtmlTextView.RemoteImageGetter());
    }

    public static String buildIframe(String url) {

        return "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" src=\""
                + url
                + "?fs=0\" frameborder=\"0\" allowfullscreen>\n"
                + "</iframe>\n";
    }

    // Video Id for YouTube API player
    public static String buildVideoId(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }


    public static void setVideoDelay(WebView videoView, String url) {
        if (videoView == null)
            return;
        videoView.setVisibility(View.VISIBLE);

        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.setWebChromeClient(new WebChromeClient());

        String html = buildIframe(url);
        videoView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", "about:blank");
    }


    @Override
    protected boolean needAddButtons() {
        return true;
    }

}
