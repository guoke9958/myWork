package com.cn.xa.qyw.ui.news;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.NewsDetail;
import com.cn.xa.qyw.entiy.NewsItem;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.Lg;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */
public class NewsDetailActivity extends DoctorBaseActivity {

    private WebView wbView;
    private View parent;
    private TextView mTitle, mSource, mAuthor, mBrowse, mCreateTime;
    private WebView mContentParent;

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
        parent = findViewById(R.id.scrollView_parent);
        parent.setVisibility(View.GONE);
        mTitle = (TextView) findViewById(R.id.title);
        mSource = (TextView) findViewById(R.id.source);
        mAuthor = (TextView) findViewById(R.id.author);
        mBrowse = (TextView) findViewById(R.id.browse);
        mCreateTime = (TextView) findViewById(R.id.create_time);
        mContentParent = (WebView) findViewById(R.id.content_parent);

        mContentParent.setWebViewClient(new SimpleWebViewClient());
        mContentParent.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContentParent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mContentParent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }

    class SimpleWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissDialog();
        }
    }

    private void getNewsDetail(int newsId) {
        HttpUtils.postDataFromServer(HttpAddress.GET_NEWS_DETAIL, "" + newsId, new NetworkResponseHandler() {
            @Override
            public void onFail(String messsage) {
                dismissDialog();
                showToast("获取新闻详情失败");
            }

            @Override
            public void onSuccess(String data) {
                NewsDetail detail = JSONObject.parseObject(data, NewsDetail.class);
                if (detail.getIsReprint() == 1) {
                    wbView.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.GONE);
                    wbView.loadUrl(detail.getContent());
                } else {

                    wbView.setVisibility(View.GONE);
                    parent.setVisibility(View.VISIBLE);

                    setView(detail);

                    mContentParent.loadUrl(HttpAddress.MAIN_ADDRESS + "news/news_detail_view?id=" + detail.getNewsId());

//                    parseContent(detail);

                }
            }
        });
    }

    private void parseContent(NewsDetail detail) {
        String xml = detail.getContent();

        TextView view = new TextView(this);
        view.setText(Html.fromHtml(xml));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentParent.addView(view, lp);

        ByteArrayInputStream tInputStringStream = null;
        try {
            if (xml != null && !xml.trim().equals("")) {
                tInputStringStream = new ByteArrayInputStream(xml.getBytes());
            }
        } catch (Exception e) {
            return;
        }

        List<NewsItem> list = new ArrayList<>();

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(tInputStringStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        // persons = new ArrayList<Person>();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("img")) {

                            NewsItem item = new NewsItem(NewsItem.IMAGE);
                            item.setContent(parser.getAttributeValue(null, "src"));
                            list.add(item);

                        } else if (name.equalsIgnoreCase("span")) {

                            NewsItem item = new NewsItem(NewsItem.SPAN);
                            item.setContent(parser.getText());
                            list.add(item);

                        } else if (name.equals("strong")) {
                            NewsItem item = new NewsItem(NewsItem.STRONG);
                            item.setContent(parser.getText());
                            list.add(item);
                        } else if (name.equals("p")) {
                            NewsItem item = new NewsItem(NewsItem.P);
                            item.setContent(parser.getText());
                            list.add(item);
                        }

                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            tInputStringStream.close();

            Lg.e("list.size()========" + list.size());

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setView(NewsDetail detail) {
        mTitle.setText(detail.getTitle());
        mSource.setText("来源：" + detail.getSource());
        mAuthor.setText("作者：" + detail.getAuthor());
        mBrowse.setText("浏览次数：" + detail.getBrowseCount());
        mCreateTime.setText("发布时间：" + DateUtils.getTimeData(detail.getCreateTime()));
    }

    private void initWebView() {
        wbView = (WebView) findViewById(R.id.webView);
        wbView.getSettings().setSupportZoom(true);
        wbView.getSettings().setBuiltInZoomControls(true);
        wbView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wbView.getSettings().setJavaScriptEnabled(true);
        wbView.setHorizontalScrollBarEnabled(true);
        wbView.setVerticalScrollBarEnabled(true);
        wbView.setWebViewClient(new MyWebViewClient());

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
        return R.layout.activity_news_detail;
    }
}
