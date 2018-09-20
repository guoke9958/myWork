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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.entiy.PullData;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.CommonAdapter;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.base.ViewHolder;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.wrapper.EmptyWrapper;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.wrapper.LoadmoreWrapper;
import com.cn.xa.qyw.ui.news.bean.NewsData;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.TmallHeaderLayout;
import com.cn.xa.qyw.utils.DensityUtils;
import com.cn.xa.qyw.view.NetworkImageHolderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.recyclerview.PullToRefreshRecyclerView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class SuperAwesomeCardFragment extends Fragment {


	private static final String ARG_POSITION = "position";

	private View view;
	private PullToRefreshRecyclerView pullToRefreshView;
	private RecyclerView myRecyclerView;
	private Activity myActivity;
	private HospitalGrade hospitalGrade;  //栏目实体
	private CommonAdapter<NewsData> mAdapter;
	private ArrayList<NewsData> myListData;
	private EmptyWrapper mEmptyWrapper;  //无数据空布局
	private HeaderAndFooterWrapper headerAndFooterWrapper;  //添加头部和尾部布局
	private LoadmoreWrapper mLoadMoreWrapper;

	private String[] images = {
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537297302711&di=8dfb98ebb43d3d04b99e0493bad11dc1&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205171759_jvyaZ.jpeg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537299482366&di=f5d2a9e367b98fe7d1fb6d4bcf931b32&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F55e736d12f2eb938913620d4df628535e5dd6fb8.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537299482366&di=3149a051c2fb817d6ed8ede5cef34d39&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D1e22316787b1cb132a643450b53d3c3b%2F48540923dd54564eb8d2beb1b9de9c82d0584f86.jpg",
			"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537299482365&di=62e72b9ebd480c0f34db4396694ce42a&imgtype=0&src=http%3A%2F%2Fp.store.itangyuan.com%2Fp%2Fbook%2Fcover%2FEg2Veg2wEt6%2FEg6tE_fWEt2uEtMuEBAWeTuP51DHhcDgjS.jpg",
			"http://www.qiuyiwang.com:8081/download/img/yd.jpg",
			"http://www.qiuyiwang.com:8081/download/img/timg.jpg"
	};


	public static SuperAwesomeCardFragment newInstance(HospitalGrade hospitalGrade) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putSerializable(ARG_POSITION, hospitalGrade);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myActivity = getActivity();
		hospitalGrade = (HospitalGrade)getArguments().getSerializable(ARG_POSITION);
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

		pullToRefreshView.setHeaderLayout(new TmallHeaderLayout(myActivity));

		RecyclerView.LayoutManager manager = new GridLayoutManager(myActivity, 1);
		myRecyclerView.setLayoutManager(manager);

		mAdapter = new CommonAdapter<NewsData>(myActivity, R.layout.fragment_super_awesome_card_item, myListData){
			@Override
			protected void convert(ViewHolder holder, NewsData data, final int position){
				SimpleDraweeView simpleDraweeView = (SimpleDraweeView)holder.getView(R.id.image_layout);
				simpleDraweeView.setImageURI(data.getUrl());
				holder.setText(R.id.layout_title,data.getTitle());
				holder.setText(R.id.layout_message,data.getMessage());
				holder.setText(R.id.layout_checked,data.getClickNum());
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
		ConvenientBanner convenientBanner = new ConvenientBanner(myActivity);
		convenientBanner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(myActivity,160f)));
		List<String> networkImages = Arrays.asList(images);
		convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
			@Override
			public NetworkImageHolderView createHolder() {
				return new NetworkImageHolderView();
			}
		},networkImages)
				.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(int position) {

					}
				})
				//设置指示器的方向
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
		convenientBanner.startTurning(2000);
		convenientBanner.setCanLoop(true);
		headerAndFooterWrapper.addHeaderView(convenientBanner);
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

		PullData pullData = new PullData();
		pullData.setId(hospitalGrade.getId());
		pullData.setPageSize(1);
		pullData.setPageCount(10);
//		String URL = "http://172.16.99.248:8080/luckdraw/api/articleist";
		String URL = HttpAddress.GET_NEW_COLUMN_ARTICLEIST;
		HttpUtils.postDataFromServer(URL, JSONObject.toJSONString(pullData), new NetworkResponseHandler() {
			@Override
			public void onFail(String messsage) {
				Log.e(hospitalGrade.getGradeName() + " messsage = ", messsage);
			}

			@Override
			public void onSuccess(String data) {
				List<NewsData> list = JSONObject.parseArray(data, NewsData.class);
				myListData.addAll(list);
				mAdapter.notifyDataSetChanged();
			}
		});
	}
}