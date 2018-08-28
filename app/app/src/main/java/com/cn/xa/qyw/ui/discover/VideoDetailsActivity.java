package com.cn.xa.qyw.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.VideoDetailAdapter;
import com.cn.xa.qyw.entiy.VideoDetail;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.web.WebViewActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */
public class VideoDetailsActivity extends DiscoverBaseActivity {

    private GridView mListView;
    private VideoDetailAdapter mAdapter;
    private String mTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTypeId = getIntent().getStringExtra("typeId");
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoDetail item = mAdapter.getItem(position);

                if (item.getIsWeb() == 1) {
                    Intent intent = getNewIntent(WebViewActivity.class);
                    intent.putExtra("url", item.getPlayUrl());
                    intent.putExtra("title", item.getSource());
                    startActivity(intent);
                } else {
                    Intent intent = getNewIntent(OnLiveActivity.class);
                    intent.putExtra("title", item.getVideoName());
                    intent.putExtra("playUrl", item.getPlayUrl());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        HttpUtils.postDataFromServer(HttpAddress.GET_ALL_VIDEO_BY_TYPE, mTypeId, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                showToast("获取信息失败");
            }

            @Override
            public void onSuccess(String data) {
                List<VideoDetail> list = JSONObject.parseArray(data, VideoDetail.class);
                mAdapter.setData(list);
            }
        });

    }

    private void initView() {
        mListView = (GridView) findViewById(R.id.videodetail_list_view);
        mAdapter = new VideoDetailAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_all_video_detail;
    }
}
