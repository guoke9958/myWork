package com.cn.xa.qyw.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.DiscoverAdapter;
import com.cn.xa.qyw.base.BaseFragment;
import com.cn.xa.qyw.entiy.item.MenuItem;
import com.cn.xa.qyw.ui.discover.DiscoverDoctorArticleActivity;
import com.cn.xa.qyw.ui.discover.DiscoverFirstAidActivity;
import com.cn.xa.qyw.ui.discover.DiscoverLotteryActivity;
import com.cn.xa.qyw.ui.discover.DiscoverNTFSActivity;
import com.cn.xa.qyw.ui.discover.DiscoverNewsActivity;
import com.cn.xa.qyw.ui.discover.OnLiveActivity;
import com.cn.xa.qyw.ui.discover.VideoTypeActivity;

/**
 * Created by Administrator on 2016/5/26.
 */
public class DiscoverFragment extends BaseFragment {

    private ListView mListView;
    private DiscoverAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_view,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initListener();
        initData();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = mAdapter.getItem(position);
                int type = item.getType();
                String itemName = item.getName();
                Intent intent = new Intent();
                intent.putExtra("title",itemName);
                switch (type){

                    case MenuItem.TYPE_DISCOVER_NEWS:
                        intent.setClass(getActivity(), DiscoverNewsActivity.class);
                        break;
                    case MenuItem.TYPE_DISCOVER_FIRST_AID:
                        intent.setClass(getActivity(),DiscoverFirstAidActivity.class);
                        break;
                    case MenuItem.TYPE_DISCOVER_LOTTERY:
                        intent.setClass(getActivity(),DiscoverLotteryActivity.class);
                        break;
                    case MenuItem.TYPE_DISCOVER_DOCTOR_ARTICLE:
                        intent.setClass(getActivity(), DiscoverDoctorArticleActivity.class);
                        break;
                    case MenuItem.TYPE_DISCOVER_NTFS:
                        intent.setClass(getActivity(), DiscoverNTFSActivity.class);
                        break;
                    case MenuItem.TYPE_DISCOVER_LIVE:
                        intent.setClass(getActivity(),VideoTypeActivity.class);
                        break;

                }
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mAdapter = new DiscoverAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }

    private void initView(View view) {
        mListView = (ListView)view.findViewById(R.id.discover_listview);
    }
}
