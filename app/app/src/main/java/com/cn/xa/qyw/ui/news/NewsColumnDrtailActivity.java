package com.cn.xa.qyw.ui.news;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.http.HttpAddress;

/**
 * app 栏目资讯详情
 * Created by amoldZhang on 2018/9/25.
 */

public class NewsColumnDrtailActivity extends DoctorBaseActivity {

    private WebView wbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("新闻详情");
        int newsId = getIntent().getIntExtra("id", 0);

        initView();
        initWebView();
        showDialog();
        getNewsDetail(newsId);
    }

    private void initView() {
        wbView = (WebView) findViewById(R.id.webView);
    }

    private void initWebView() {
        wbView.getSettings().setSupportZoom(true);
        wbView.getSettings().setBuiltInZoomControls(true);
        wbView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wbView.getSettings().setJavaScriptEnabled(true);
        wbView.setHorizontalScrollBarEnabled(true);
        wbView.setVerticalScrollBarEnabled(true);
        wbView.setWebViewClient(new MyWebViewClient());
    }

    private void getNewsDetail(int newsId) {
        wbView.loadUrl(HttpAddress.GET_NEW_WEB_ARTICLE  + newsId + ".htm");
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissDialog();
        }
    }


    @Override
    public int getChildLayoutId() {
        return R.layout.activity_news_column;
    }
}
