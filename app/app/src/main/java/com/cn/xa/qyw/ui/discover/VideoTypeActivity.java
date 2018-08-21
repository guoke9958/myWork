package com.cn.xa.qyw.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.VideoTypeAdapter;
import com.cn.xa.qyw.entiy.VideoDetail;
import com.cn.xa.qyw.entiy.VideoType;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */
public class VideoTypeActivity extends DiscoverBaseActivity {

    private GridView mListView;
    private VideoTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoType item = mAdapter.getItem(position);
                Intent intent = getNewIntent(VideoDetailsActivity.class);
                intent.putExtra("title",item.getTypeName());
                intent.putExtra("typeId",item.getId()+"");
                startActivity(intent);
            }
        });
    }

    private void initData() {
        HttpUtils.getDataFromServer(HttpAddress.GET_ALL_VIDEO_TYPE, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                showToast("获取视频分类失败");
            }

            @Override
            public void onSuccess(String data) {
                List<VideoType> list = JSONObject.parseArray(data,VideoType.class);
                mAdapter.setData(list);
            }
        });
    }

    private void initView() {
        mListView = (GridView) findViewById(R.id.video_gridview);
        mAdapter = new VideoTypeAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_video_all_type;
    }
}
