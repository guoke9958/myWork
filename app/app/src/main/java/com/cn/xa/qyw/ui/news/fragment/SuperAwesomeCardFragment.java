/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cn.xa.qyw.ui.news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.CommonAdapter;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.base.ViewHolder;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.wrapper.EmptyWrapper;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.wrapper.LoadmoreWrapper;
import com.cn.xa.qyw.ui.news.bean.NewsData;
import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.recyclerview.PullToRefreshRecyclerView;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class SuperAwesomeCardFragment extends Fragment {


	private static final String ARG_POSITION = "position";

	private int position;
	private View view;
	private PullToRefreshRecyclerView pullToRefreshView;
	private RecyclerView myRecyclerView;
	private Activity myActivity;
	private CommonAdapter<NewsData> mAdapter;
	private ArrayList<NewsData> myListData;
	private EmptyWrapper mEmptyWrapper;  //无数据空布局
	private HeaderAndFooterWrapper headerAndFooterWrapper;  //添加头部和尾部布局
	private LoadmoreWrapper mLoadMoreWrapper;

	public static SuperAwesomeCardFragment newInstance(int position) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myActivity = getActivity();
		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = LayoutInflater.from(getActivity()).inflate(R.layout.layout_super_awesome_card_fragment,container,false);
        getData();
        initView();
		return view;
	}

	private void initView() {
		pullToRefreshView = (PullToRefreshRecyclerView)view.findViewById(R.id.pull_to_refresh_recycler_view);
		myRecyclerView = pullToRefreshView.getRefreshableView();

		RecyclerView.LayoutManager manager = new GridLayoutManager(myActivity, 1);
		myRecyclerView.setLayoutManager(manager);

		mAdapter = new CommonAdapter<NewsData>(myActivity, R.layout.fragment_super_awesome_card_item, myListData){
			@Override
			protected void convert(ViewHolder holder, NewsData data, final int position){
				SimpleDraweeView simpleDraweeView = (SimpleDraweeView)holder.getView(R.id.image_layout);
				simpleDraweeView.setImageURI(data.getUrl());
				holder.setText(R.id.layout_title,data.getTitle());
				holder.setText(R.id.layout_message,data.getTitle());
			}
		};

		//可以设置一个EmptyView
		initEmptyView();

		headerAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
         //初始化RecyclerView的头部 ,可以添加一个或多个头部
		initRecyclerViewHead();
		pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
			@Override
			public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						// Call onRefreshComplete when the list has been refreshed.
						pullToRefreshView.onRefreshComplete();
					}
				}, 1500);
			}
		});

		mLoadMoreWrapper = new LoadmoreWrapper(headerAndFooterWrapper);
		mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
		mLoadMoreWrapper.setOnLoadMoreListener(new LoadmoreWrapper.OnLoadMoreListener(){
			@Override
			public void onLoadMoreRequested(){
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						mLoadMoreWrapper.notifyDataSetChanged();
						// Call onRefreshComplete when the list has been refreshed.
						pullToRefreshView.onRefreshComplete();
					}
				}, 1500);
			}
		});
		myRecyclerView.setAdapter(mLoadMoreWrapper);
	}

	private void initRecyclerViewHead() {
//		SimpleDraweeView simpleDraweeView = new SimpleDraweeView(myActivity);
//		simpleDraweeView.set
//		headerAndFooterWrapper.addHeaderView(simpleDraweeView);
	}

	private void initEmptyView(){
//		if (mEmptyWrapper == null){
//			mEmptyWrapper = new EmptyWrapper(mAdapter);
//			mEmptyWrapper.setEmptyView(LayoutInflater.from(myActivity).inflate(R.layout.layout_no_data, myRecyclerView, false));
//		}
	}

	/**
	 * 数据初始化
	 */
	public void getData() {
		myListData = new ArrayList<>();
		for(int i=0;i<10;i++){
			NewsData mewsData = new NewsData();
			mewsData.setUrl("");
			mewsData.setTitle(i + "setTitle " + "123");
			mewsData.setMessage(i + "setMessage " + "123");
			myListData.add(mewsData);
		}
	}
}