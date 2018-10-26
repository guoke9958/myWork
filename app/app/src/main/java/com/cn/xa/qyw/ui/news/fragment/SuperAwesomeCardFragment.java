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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.factory.EmptyNoticeLayout;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.news.NewsColumnActivity;
import com.cn.xa.qyw.ui.news.NewsColumnDrtailActivity;
import com.cn.xa.qyw.ui.news.bean.NewsData;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.BaseRecyclerAdapter;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.CommonAdapter;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.HeadPullRecyclerView;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.TmallFooterLayout;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.TmallHeaderLayout;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.WrapRecyclerView;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.base.ViewHolder;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.DensityUtils;
import com.cn.xa.qyw.view.NetworkImageHolderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.example.library.PullToRefreshBase;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SuperAwesomeCardFragment extends Fragment {


	private static final String ARG_POSITION = "position";

	private View view;
	private HeadPullRecyclerView pullToRefreshView;
	private WrapRecyclerView myRecyclerView;

	private HospitalGrade hospitalGrade;  //栏目实体
	private CommonAdapter<NewsData> mAdapter;
	private LinkedList<NewsData> myListData = new LinkedList<>();

	private int pageSize = 1;
	List<String> networkImages = new ArrayList<>();
	private ConvenientBanner convenientBanner;
	private Activity myActivity;
	private EmptyNoticeLayout emptyNoticeLayout;

//	public static SuperAwesomeCardFragment newInstance(HospitalGrade hospitalGrade) {
//		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
//		Bundle b = new Bundle();
//		b.putSerializable(ARG_POSITION, hospitalGrade);
//		f.setArguments(b);
//		return f;
//	}

	public Fragment setArguments(HospitalGrade hospitalGrade) {
		Bundle b = new Bundle();
		b.putSerializable(ARG_POSITION, hospitalGrade);
		setArguments(b);
		return this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myActivity = getActivity();
		hospitalGrade = (HospitalGrade)getArguments().getSerializable(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		try {
			view  = inflater.inflate(R.layout.layout_super_awesome_card_fragment,container,false);
			initView();
			getNewsData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		try {
			if (!hidden){
				if (convenientBanner != null){
					convenientBanner.startTurning(2000);
				}
				convenientBanner.notifyDataSetChanged();
			}else{
				if (convenientBanner != null){
					convenientBanner.stopTurning();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 界面布局初始化
	 */
	private void initView() {
		emptyNoticeLayout = (EmptyNoticeLayout)view.findViewById(R.id.data_empty_layout);
		emptyNoticeLayout.setBaseLoadEmpty(new EmptyNoticeLayout.ClickEmpty() {
			@Override
			public void onClickListener(View v) {
				((NewsColumnActivity)getActivity()).showDialog();
				getNewsData();
			}
		});
		pullToRefreshView = (HeadPullRecyclerView)view.findViewById(R.id.pull_to_refresh_recycler_view);
		//设置刷新模式 ，both代表支持上拉和下拉，pull_from_end代表上拉，pull_from_start代表下拉
		pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
		pullToRefreshView.setHeaderLayout(new TmallHeaderLayout(getContext()));
		pullToRefreshView.setFooterLayout(new TmallFooterLayout(getContext()));

		initRefreshView();

		pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<WrapRecyclerView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						// Call onRefreshComplete when the list has been refreshed.
						pageSize = 1;
						getNewsData();
					}
				}, 1500);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<WrapRecyclerView> refreshView) {
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						// Call onRefreshComplete when the list has been refreshed.
						pageSize ++ ;
						getNewsData();
					}
				}, 1500);
			}

		});
	}

	/**
	 * 初始化 RefreshView
	 */
	private void initRefreshView() {
		myRecyclerView = pullToRefreshView.getRefreshableView();
		myRecyclerView.setLayoutManager(new LinearLayoutManager(myActivity));
		myRecyclerView.setLayoutManager(new GridLayoutManager(myActivity, 1));

		mAdapter = new CommonAdapter<NewsData>(myActivity, R.layout.fragment_super_awesome_card_item, myListData){
			@Override
			protected void convert(ViewHolder holder, NewsData data, int position) {
				if (data.getVideoState() == 0){
					holder.getView(R.id.image_news_veio).setVisibility(View.GONE);
				}else if (data.getVideoState() == 1){
					holder.getView(R.id.image_news_veio).setVisibility(View.VISIBLE);
				}
				SimpleDraweeView simpleDraweeView = (SimpleDraweeView)holder.getView(R.id.image_layout);
				simpleDraweeView.setImageURI(data.getTumb());
				holder.setText(R.id.layout_title,data.getTitle());
				holder.setText(R.id.layout_message,data.getSummary());
				holder.setText(R.id.layout_checked,"浏览量：" + data.getClickNum());
				holder.setText(R.id.layout_time, DateUtils.convertToTime(data.getUpdateTime())+ "");
			}
		};
		initRecyclerViewHead();

		myRecyclerView.setAdapter(mAdapter);

		mAdapter.setOnRecyclerViewListener(new BaseRecyclerAdapter.OnRecyclerViewListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(myActivity, NewsColumnDrtailActivity.class);
				intent.putExtra("id", mAdapter.getItemArrayLists().get(position).getArticleId());
				myActivity.startActivity(intent);
			}

			@Override
			public boolean onItemLongClick(int position) {
				return false;
			}
		});


	}


	/**
	 *  联网更新数据
	 */
	private void getNewsData() {
		RequestParams params = new RequestParams();
		params.put("id",hospitalGrade.getId());
		params.put("pageSize",pageSize);
		params.put("pageCount",10);
		HttpUtils.getDataFromServer(HttpAddress.GET_NEW_COLUMN_ARTICLEIST, params, new NetworkResponseHandler() {
			@Override
			public void onFail(String messsage) {
				((NewsColumnActivity)getActivity()).dismissDialog();
				pullToRefreshView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				Log.e(hospitalGrade.getGradeName() + " messsage = ", messsage);
				emptyNoticeLayout.showErrorView(pullToRefreshView);
			}

			@Override
			public void onSuccess(String data) {
				((NewsColumnActivity)getActivity()).dismissDialog();
				List<NewsData> list = JSONObject.parseArray(data, NewsData.class);
				if (list.size() == 0 && pageSize == 1){
					emptyNoticeLayout.showEmptyView(pullToRefreshView);
					return;
				}
				emptyNoticeLayout.setVisibility(View.GONE);
				pullToRefreshView.setVisibility(View.VISIBLE);
				if (pageSize == 1){
					myListData.clear();
					mAdapter.notifyDataSetChanged();
					myListData.addAll(list);
//					initRecyclerViewHead();
				}else{
					myListData.addAll(list);
				}
				mAdapter.notifyDataSetChanged();
				// Call onRefreshComplete when the list has been refreshed.
				pullToRefreshView.onRefreshComplete();
			}
		});
	}

	/**
	 *  初始化RecyclerView的头部 ,可以添加一个或多个头部
	 *
	 *  轮播图布局初始化
	 */
	private void initRecyclerViewHead() {
		try {
			networkImages.clear();
			mAdapter.notifyDataSetChanged();
			if (networkImages.size() == 0){
				networkImages.add("http://www.qiuyiwang.com:8081/download/img/yd.jpg");
				networkImages.add("http://www.qiuyiwang.com:8081/download/img/timg.jpg");
			}
			if (networkImages.size() != 0){
				if (convenientBanner != null){
					myRecyclerView.removeView(convenientBanner);
				}
				setHeadView();
			}
			mAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e(SuperAwesomeCardFragment.class.getName() + " initRecyclerViewHead()",e.toString());
		}
	}

	private void setHeadView() {
		try {
			convenientBanner = new ConvenientBanner(myActivity);
			convenientBanner.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(myActivity,160f)));
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
							//						Intent intent = new Intent(myActivity, NewsDetailActivity.class);
							//						intent.putExtra("id", id);
							//						myActivity.startActivity(intent);
						}
					})
					//设置指示器的方向
					.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
			convenientBanner.startTurning(2000);
			convenientBanner.setCanLoop(true);
			myRecyclerView.addHeaderView(convenientBanner);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if (convenientBanner != null){
			convenientBanner.stopTurning();
			convenientBanner = null;
		}
	}
}