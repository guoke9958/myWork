package com.cn.xa.qyw.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.entiy.NewsDetail;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.news.fragment.QuickContactFragment;
import com.cn.xa.qyw.ui.news.fragment.SuperAwesomeCardFragment;
import com.cn.xa.qyw.view.PagerSlidingTabStrip;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据栏目来显示新闻资讯
 */
public class NewsColumnActivity extends DoctorBaseActivity {


    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private String mGrade;
    private long mGradeId;
    private List<HospitalGrade> listColumn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGrade = getIntent().getStringExtra("grade");
        mGradeId = getIntent().getLongExtra("grade_id",0);

        mToolbarTitle.setText(mGrade);
        getNewsData();
    }

    private void getNewsData() {
        try {
            String URL =HttpAddress.GET_NEW_COLUMN;
//            String URL = "http://172.16.99.248:8080/luckdraw/api/categorylist";
            HttpUtils.getDataFromServer(URL, new NetworkResponseHandler() {
                @Override
                public void onFail(String messsage) {
                    Log.e(mGrade + " messsage = ", messsage);
                }

                @Override
                public void onSuccess(String data) {
                    listColumn = JSONObject.parseArray(data, HospitalGrade.class);
                    initView();
                }
            });
        }catch (Exception e){
            Log.e("",e.toString());
        }
    }

    private void initView() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listColumn.get(position).getGradeName();
        }

        @Override
        public int getCount() {
            return listColumn.size();
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return QuickContactFragment.newInstance();
            }else{
                return SuperAwesomeCardFragment.newInstance(listColumn.get(position));
            }
        }

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_news_column;
    }
}
