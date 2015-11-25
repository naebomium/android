package com.mobium.reference.fragments.support;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobium.client.api.networking.WebHelper;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.fragments.BasicLoadableFragment;
import com.mobium.reference.utils.executing.ExecutingException;

import java.io.IOException;

/**
 *
 *
 * Date: 30.11.12
 * Time: 23:37
 */
public class WebStaticFragment extends BasicLoadableFragment {
    private String url;
    private String content;
    private String pageTitle;
    private String contentId;
    private String localContentId;
    private String newsId;

    private WebView webView;
    private View progress;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getLocalContentId() {
        return localContentId;
    }

    public void setLocalContentId(String localContentId) {
        this.localContentId = localContentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    protected View getContentView() {
        return webView;
    }

    @Override
    protected View getProgressView() {
        return progress;
    }

    @Override
    protected String getTitle() {
        if (pageTitle == null) {
            return "Информация";
        } else {
            return pageTitle;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView!= null)
            webView.onResume();
        if (content != null) {
            loadContent();
        }
    }

    @Override
    protected boolean needLoading() {
        return content == null;
    }

    @Override
    protected void loadInBackground() throws ExecutingException {
        if (localContentId != null) {
            try {
                getApplication().getAssets().open("web/" + localContentId + ".html");
            } catch (IOException e) {
                throw new ExecutingException(getString(R.string.error_connection));
            }
        } else if (url != null) {
            try {
                content = WebHelper.downloadString(url, ReferenceApplication.getInstance().okHttpClient);
            } catch (Exception e) {
                throw new ExecutingException(getString(R.string.error_connection));
            }
        } else if (contentId != null) {
            content = getApplication().getExecutor().getHtml(contentId);
        } else if (newsId != null) {
            content = getApplication().getExecutor().getNewsRecord(newsId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null)
            webView.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null)
            webView.onPause();
    }

    @Override
    protected void afterLoaded() {
        loadContent();
    }

    @Override
    protected void alreadyLoaded() {
        loadContent();
    }

    private void loadContent() {
        if (url != null) {
            webView.loadDataWithBaseURL(url, content, "text/html", "utf-8", url);
        } else {
            webView.loadDataWithBaseURL("", content, "text/html", "utf-8", "");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (url != null) {
            outState.putString("url", url);
        }
        if (content != null) {
            outState.putString("content", content);
        }
        if (contentId != null) {
            outState.putString("contentId", contentId);
        }
        if (pageTitle != null) {
            outState.putString("pageTitle", pageTitle);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("url")) {
                url = savedInstanceState.getString("url");
            }

            if (savedInstanceState.containsKey("content")) {
                content = savedInstanceState.getString("content");
            }

            if (savedInstanceState.containsKey("contentId")) {
                contentId = savedInstanceState.getString("contentId");
            }

            if (savedInstanceState.containsKey("pageTitle")) {
                pageTitle = savedInstanceState.getString("pageTitle");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_web_static, container, false);
        webView = (WebView) res.findViewById(R.id.webView);
        progress = res.findViewById(R.id.progress);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!v.hasFocus()) {
                        v.requestFocus();
                    }
                    break;
            }
            return false;
        });

        webView.setWebViewClient(new LoadLinkWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        return res;
    }

    @Override
    public boolean onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    private class LoadLinkWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("#MOBIUM_EXTERNAL")) {
                setUrl(url);
                url = url.replace("#MOBIUM_EXTERNAL", "");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                getDashboardActivity().startActivity(Intent.createChooser(intent, "Выберите браузер"));

            } else  if (!url.startsWith("http")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                try {
                    startActivity(intent);
                    setUrl(url);
                } catch (ActivityNotFoundException e) {
                }
            } else {
                view.loadUrl(url);
                setUrl(url);
            }

            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (WebStaticFragment.this.url != null && "about:blank".equals(url)) {
                getActivity().onBackPressed();
            }
            super.onPageStarted(view, url, favicon);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    public static WebStaticFragment getInstance(String contentId, String pageTitle) {
        WebStaticFragment result = new WebStaticFragment();
        result.setPageTitle(pageTitle);
        result.setContentId(contentId);
        return result;
    }

    public static WebStaticFragment getInstanceToUrl(String url, String pageTitle) {
        WebStaticFragment result = new WebStaticFragment();
        result.setPageTitle(pageTitle);
        result.setUrl(url);
        return result;
    }

    public static WebStaticFragment getInstanceToNewsItem(String newsId, String pageTitle) {
        WebStaticFragment result = new WebStaticFragment();
        result.setNewsId(newsId);
        result.setPageTitle(pageTitle);
        return result;
    }


}
