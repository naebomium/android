package com.mobium.reference.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.mobium.reference.activity.RequestCodes;
import com.mobium.reference.productPage.DetailsFactory;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.video.VideoEnabledWebChromeClient;
import com.mobium.reference.utils.video.VideoEnabledWebView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  on 16.10.15.
 */
public class VideoPlayer implements DetailsFactory.DetailsLifeCycle, FragmentUtils.BackButtonHandler {
    private final FrameLayout viewGroup;
    private final ViewGroup fullScreenContainer;
    private final VideoEnabledWebView videoEnabledWebView = null;
    private final VideoEnabledWebChromeClient chromeClient = null;
    private final float videoProportion = 16f / 9f;

    private final static Pattern pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE);

    private final WebView view;

    public VideoPlayer(FrameLayout videoContainer, ViewGroup fullScreenContainer) {
        this.viewGroup = videoContainer;
        this.fullScreenContainer = fullScreenContainer;

        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        viewGroup.addView(videoEnabledWebView, p);

        view = new WebView(videoContainer.getContext());
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebChromeClient(new WebChromeClient());
        viewGroup.addView(view, p);
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int width = viewGroup.getWidth();
                viewGroup.getLayoutParams().height = (int) (width / videoProportion);
                viewGroup.requestLayout();
                viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void playYoutubeVideo(String videoUrl) {

        int h = fullScreenContainer.getHeight();
        int w = fullScreenContainer.getWidth();

        String width = "width=\"" + w + "px\"";
        String heght = "height=\"" + h + "px\"";
        String data_html = "<!DOCTYPE HTML> \n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"\n" +
                "    height: 100%;\n" +
                "\">\n" +
                "<head></head> \n" +
                "\t\t\t<body style=\"margin:0 0 0 0; width:100%; height:100%; padding:0 0 0 0;\"> \n" +
                "\t\t\t\t<iframe width=\"100%\" height=\"100%\" \n" +
                "src=\" " + videoUrl + "\" " +
                "\n" +
                "\t\t\t\t\tframeborder=\"0\">\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t</iframe> \n" +
                "\t\t\t\n" +
                " </body></html> ";

        String videoHtml = "<html><body style=\"margin:0 0 0 0; padding:0 0 0 0;\"><iframe class=\"youtube-player\" " +
                "type=\"text/html\" " +
                "width=\"100%\" " +
                "height=\"100%\" " +
                "frameborder=\"0\" framespacing=\"0\" border=\"0\"" +
                "src=\" " + videoUrl + "\" " +
                "frameborder=\"0\">" +
                "</body>" +
                "</html>";

        String sipmleIframe = "<iframe " +
                "width=\"100%\" " +
                "height=\"100%\" " +
                "src=\" " + videoUrl + "\" " +
                "frameborder=\"0\" " +
                "allowfullscreen>" +
                "</iframe>\n";

        view.loadData(data_html, "text/html", "utf-8");
        String videoId = extractYTId(videoUrl);


        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) viewGroup.getContext(), Config.get().getApplicationData().getApiKeyGoogleYoutubeApi(), videoId, 0, true, false);
        if (YouTubeIntents.canResolvePlayVideoIntent(viewGroup.getContext())) {

            View view = new View(viewGroup.getContext());
            viewGroup.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            view.setOnClickListener(v -> ((Activity) viewGroup.getContext()).startActivityForResult(intent, RequestCodes.VIDEO_PLAY));
        }
    }

    @Override
    public void onPause() {
        view.onPause();
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;

        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }


    @Override
    public void onDestroy() {
        view.destroy();
    }

    @Override
    public void onResume() {
        view.onResume();
    }

    @Override
    public boolean onBackPressed() {
        if (view.canGoBack()) {
            view.goBack();
            return true;
        }
        return false;
    }
}


//        videoEnabledWebView = new VideoEnabledWebView(videoContainer.getContext());
//        chromeClient = new VideoEnabledWebChromeClient(videoContainer, fullScreenContainer, null, videoEnabledWebView);
//        videoEnabledWebView.setWebChromeClient(chromeClient);
//
//
//
//        chromeClient.setOnToggledFullscreen(fullscreen -> {
//            Context context = videoContainer.getContext();
//            if (context != null && context instanceof Activity) {
//                Activity activity = (Activity) context;
//                Window window = activity.getWindow();
//                if (fullscreen) {
//                    WindowManager.LayoutParams attrs = window.getAttributes();
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//                    window.setAttributes(attrs);
//                    if (android.os.Build.VERSION.SDK_INT >= 14) {
//                        //noinspection all
//                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//                    }
//
//                } else {
//                    WindowManager.LayoutParams attrs = window.getAttributes();
//                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//                    window.setAttributes(attrs);
//                    if (android.os.Build.VERSION.SDK_INT >= 14) {
//                        //noinspection all
//                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                    }
//                }
//
//                if (activity instanceof MainDashboardActivity) {
//                    MainDashboardActivity a = (MainDashboardActivity) activity;
//                    a.getNavigatinLayout().setDrawerLockMode(fullscreen ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
//                }
//            }
//        });
