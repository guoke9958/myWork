package com.cn.xa.qyw.ui.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.ui.discover.DiscoverLotteryDrawActivity;
import com.cn.xa.qyw.ui.login.LoginActivity;
import com.tencent.android.tpush.horse.Tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * app 栏目资讯详情
 * Created by amoldZhang on 2018/9/25.
 */

public class NewsColumnDrtailActivity extends DoctorBaseActivity {

    private WebView wbView;
    private int newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("详情");
        newsId = getIntent().getIntExtra("id", 0);

        initView();
        initWebView();
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

        wbView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissDialog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                dismissDialog();
            }
        });

        wbView.setWebChromeClient(new WebChromeClient() {

            /**
             * 当WebView加载之后，返回 HTML 页面的标题 Title
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) {
                    dismissDialog();
                } else {
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        onRefreshWEBView();
    }


    /**
     * webView 刷新
     */
    private void onRefreshWEBView() {
        showDialog();
        Log.e("资讯详情链接", "getNewsDetail: URL = " + HttpAddress.GET_NEW_WEB_ARTICLE.replace("{articleId}",newsId + ""));
        if (DoctorApplication.mUser != null) {
            wbView.loadUrl(HttpAddress.GET_NEW_WEB_ARTICLE.replace("{articleId}",newsId + "")+ "?userId=" + DoctorApplication.mUser.getUserId());
        }else{
            wbView.addJavascriptInterface(new AndroidtoJs(), "js2Android");//AndroidtoJS类对象映射到js的test对象
            wbView.loadUrl(HttpAddress.GET_NEW_WEB_ARTICLE.replace("{articleId}",newsId + ""));
        }
    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        /**
         * 没有登录
         */
        @JavascriptInterface
        public void setLogin() {
            //需要在 html 中写的代码
            new MaterialDialog.Builder(instance)
                    .title("登录提示")
                    .content("您未登录应用，登录之后才可以享受此抽奖活动！")
                    .positiveText("登陆")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            instance.startActivity(new Intent(instance, LoginActivity.class));
                        }
                    })
                    .negativeText("取消")
                    .show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        wbView.reload ();
        wbView.removeAllViews();
        wbView.destroy();
        finish();
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_news_column_drtail;
    }
}
