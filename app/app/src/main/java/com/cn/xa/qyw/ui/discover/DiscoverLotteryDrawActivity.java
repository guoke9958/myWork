package com.cn.xa.qyw.ui.discover;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.dialog.DoctorBaseDialog;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.ui.login.LoginActivity;

/**
 * 抽奖活动界面
 */
public class DiscoverLotteryDrawActivity extends DiscoverBaseActivity {

    WebView wbDetails;
    private String news_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
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
        if (DoctorApplication.mUser != null) {
            news_url = HttpAddress.HOST_2 + "luckdraw/index.html?userId=" + DoctorApplication.mUser.getUserId();
            // 通过addJavascriptInterface()将Java对象映射到JS对象
            //参数1：Javascript对象名
            //参数2：Java对象名
            wbDetails.addJavascriptInterface(new AndroidtoJs(), "js2Android");//AndroidtoJS类对象映射到js的test对象
            wbDetails.loadUrl(news_url);
        }else{
            new MaterialDialog.Builder(this)
                    .title("登录提示")
                    .content("您未登录应用，登录之后才可以享受此抽奖活动！")
                    .positiveText("登陆")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                        }
                    })
                    .negativeText("取消")
                    .show();
        }
    }

    private void initView() {
        try {
            wbDetails = (WebView)findViewById(R.id.my_web_view);
            initWebView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * webView初始化
     * @throws Exception
     */
    public void initWebView() {
        wbDetails.onResume();
        wbDetails.setFocusableInTouchMode(false);
        wbDetails.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings settings = wbDetails.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(false); //打开页面时， 自适应屏幕
        settings.setLoadWithOverviewMode(true);//打开页面时， 自适应屏幕
        settings.setSupportZoom(false);// 用于设置webview放大
        settings.setBuiltInZoomControls(false);



        //设置webView的缓存
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);

        wbDetails.setWebViewClient(new WebViewClient() {
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

        wbDetails.setWebChromeClient(new WebChromeClient() {

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


    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void eventTobuyCoal(String goodsId) {

        }

        /**
         * 积分不足
         * @param userId
         *
         */
        @JavascriptInterface
        public void integralRecharge(String userId) {
            //需要在 html 中写的代码
            showToast("积分不足，请充值");
        }

        /**
         * 未登录回调
         */
        @JavascriptInterface
        public void open_login_page() {

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wbDetails.loadUrl("about:blank");// 避免出现默认的错误界面
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_discover_lottery_draw;
    }
}
