package com.cn.xa.qyw.ui.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.factory.EmptyNoticeLayout;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.news.adapter.MyViewPagerAdapter;
import com.cn.xa.qyw.ui.news.fragment.SuperAwesomeCardFragment;
import com.cn.xa.qyw.view.PagerSlidingTabStrip;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据栏目来显示新闻资讯
 */
public class NewsColumnActivity extends DoctorBaseActivity {

    private EmptyNoticeLayout emptyNoticeLayout;
    private String mGrade;
    private long mGradeId;
    private List<HospitalGrade> listColumn = new ArrayList<>();
    private MyViewPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGrade = getIntent().getStringExtra("grade");
        mGradeId = getIntent().getLongExtra("grade_id",0);

        mToolbarTitle.setText(mGrade);

        emptyNoticeLayout = (EmptyNoticeLayout)findViewById(R.id.data_empty_layout);
        emptyNoticeLayout.setBaseLoadEmpty(new EmptyNoticeLayout.ClickEmpty() {
            @Override
            public void onClickListener(View v) {
                getNewsData();
            }
        });
        getNewsData();
    }

    private void getNewsData() {
        try {
            showDialog();
            RequestParams params = new RequestParams();
            params.put("appId",mGradeId);
            HttpUtils.getDataFromServer(HttpAddress.GET_NEW_COLUMN,params, new NetworkResponseHandler() {
                @Override
                public void onFail(String messsage) {
                    dismissDialog();
                    Log.e(mGrade + " messsage = ", messsage);
                    emptyNoticeLayout.showErrorView(findViewById(R.id.main_layout));
                }

                @Override
                public void onSuccess(String data) {
                    dismissDialog();
                    listColumn = JSONObject.parseArray(data, HospitalGrade.class);
                    if (listColumn.size() != 0){
                        emptyNoticeLayout.setVisibility(View.GONE);
                        findViewById(R.id.main_layout).setVisibility(View.VISIBLE);

                        mPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),listColumn);
                        initView();
                    }else{
                        emptyNoticeLayout.showEmptyView(findViewById(R.id.main_layout));
                    }
                }
            });
        }catch (Exception e){
            Log.e("",e.toString());
        }
    }

    private void initView() {
        try {
            mTabLayout = (TabLayout) findViewById(R.id.layout_category_title);
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_news_column;
    }
}
