package com.cn.xa.qyw.ui.news.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.cn.xa.qyw.ui.news.adapter.quickadapter.BaseAdapterHelper;
import com.cn.xa.qyw.ui.news.adapter.quickadapter.QuickAdapter;
import com.cn.xa.qyw.ui.news.bean.NewsData;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.TmallFooterLayout;
import com.cn.xa.qyw.ui.news.wrapRecyclerview.TmallHeaderLayout;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.DensityUtils;
import com.cn.xa.qyw.view.NetworkImageHolderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.example.library.PullToRefreshBase;
import com.example.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class  QuickContactFragment extends Fragment {

	private String ARG_POSITION = "ARG_POSITION";
	private Activity myActivity;
	private HospitalGrade hospitalGrade;
	private ConvenientBanner convenientBanner;
	private EmptyNoticeLayout emptyNoticeLayout;
	private PullToRefreshListView pullToListView;
	private int pageSize = 1;
	List<String> networkImages = new ArrayList<>();
	private QuickAdapter<NewsData> mAdapter;
	private LinkedList<NewsData> myListData = new LinkedList<>();

	public Fragment setArguments(HospitalGrade hospitalGrade) {
		Bundle b = new Bundle();
		b.putSerializable(ARG_POSITION, hospitalGrade);
		setArguments(b);
		return this;
	}


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myActivity = getActivity();
		hospitalGrade = (HospitalGrade)getArguments().getSerializable(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_quick_contact, container, false);
		initView(root);
		getNewsData();
		return root;
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
	private void initView(View root) {
		emptyNoticeLayout = (EmptyNoticeLayout)root.findViewById(R.id.data_empty_layout);
		emptyNoticeLayout.setBaseLoadEmpty(new EmptyNoticeLayout.ClickEmpty() {
			@Override
			public void onClickListener(View v) {
				((NewsColumnActivity)getActivity()).showDialog();
				getNewsData();
			}
		});
		pullToListView = (PullToRefreshListView)root.findViewById(R.id.pull_to_refresh_list_view);


        /**
         * Mode.BOTH：同时支持上拉下拉
         * Mode.PULL_FROM_START：只支持下拉Pulling Down
         * Mode.PULL_FROM_END：只支持上拉Pulling Up
         * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。
         * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。
         * 当然也可以设置为OnRefreshListener2，但是Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法，
         * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.
         */
		pullToListView.setMode(PullToRefreshBase.Mode.BOTH);
		pullToListView.setHeaderLayout(new TmallHeaderLayout(getContext()));
		pullToListView.setFooterLayout(new TmallFooterLayout(getContext()));

		initRefreshView();

		pullToListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
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
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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
		mAdapter = new QuickAdapter<NewsData>(myActivity, R.layout.fragment_super_awesome_card_item, myListData){
			@Override
			protected void convert(BaseAdapterHelper helper, NewsData item, int position) {
				SimpleDraweeView simpleDraweeView = (SimpleDraweeView)helper.getView(R.id.image_layout);
				if (item.getVideoState() == 0){
					simpleDraweeView.setImageURI(item.getTumb());
					helper.getView(R.id.image_news_veio).setVisibility(View.GONE);
				}else if (item.getVideoState() == 1){
//					helper.getView(R.id.image_news_veio).setVisibility(View.VISIBLE);
					simpleDraweeView.setImageDrawable(getResources().getDrawable(R.drawable.rc_file_icon_video));
				}


				helper.setText(R.id.layout_title,item.getTitle());
				helper.setText(R.id.layout_message,item.getSummary());
				helper.setText(R.id.layout_checked,"浏览量：" + item.getClickNum());
				helper.setText(R.id.layout_time, DateUtils.convertToTime(item.getUpdateTime())+ "");
			}
		};

		try {
			initRecyclerViewHead();
			pullToListView.setAdapter(mAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		pullToListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(myActivity, NewsColumnDrtailActivity.class);
				intent.putExtra("id", mAdapter.getItem(position - 2).getArticleId());
				myActivity.startActivity(intent);
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
				pullToListView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				Log.e(hospitalGrade.getGradeName() + " messsage = ", messsage);
				emptyNoticeLayout.showErrorView(pullToListView);
			}

			@Override
			public void onSuccess(String data) {
				List<NewsData> list = JSONObject.parseArray(data, NewsData.class);
				if (list.size() == 0 && pageSize == 1){
					emptyNoticeLayout.showEmptyView(pullToListView);
					return;
				}
				emptyNoticeLayout.setVisibility(View.GONE);
				pullToListView.setVisibility(View.VISIBLE);
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
				pullToListView.onRefreshComplete();
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
					pullToListView.removeView(convenientBanner);
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
			//将RadioGroup布局添加到listView顶部
			pullToListView.getRefreshableView().addHeaderView(convenientBanner);
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
