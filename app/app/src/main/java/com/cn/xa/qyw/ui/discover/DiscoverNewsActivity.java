package com.cn.xa.qyw.ui.discover;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.DiscoverNewsAdapter;
import com.cn.xa.qyw.entiy.News;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;

import java.util.List;

/**
 * Created by 409160 on 2016/7/8.
 */
public class DiscoverNewsActivity extends DiscoverBaseActivity {

    private ListView mListView;
    private DiscoverNewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();

        getData();

    }

    private void getData() {
        showDialog();
        News news = new News();
        news.setCreateTime(DateUtils.getCurrentTimestamp());

        String data = JSONObject.toJSONString(news);

        HttpUtils.postDataFromServer(HttpAddress.GET_NEWS_ALL,data , new NetworkResponseHandler() {
            @Override
            public void onFail(String messsage) {
                dismissDialog();
                showToast("获取新闻失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                List<News> list = JSONObject.parseArray(data,News.class);
                mAdapter.setData(list);
            }
        });
    }

    private void initView() {
        mListView = (ListView)findViewById(R.id.news_listview);
        mAdapter = new DiscoverNewsAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(getNewIntent(NewsContentActivity.class));
            }
        });
    }

    private void initData() {
        
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_discover_news;
    }
}
