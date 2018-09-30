package com.cn.xa.qyw.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.entiy.NewsDetail;
import com.cn.xa.qyw.factory.EmptyNoticeLayout;
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

    private EmptyNoticeLayout emptyNoticeLayout;
    private String mGrade;
    private long mGradeId;
    private List<HospitalGrade> listColumn = new ArrayList<>();

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
        // 初始化 ViewPager 和 Adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        // 绑定 PagerSlidingTabStrip 到 ViewPager 上
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        setTabls(tabs);
    }

    /**
     * 根据你的需要修改下面的值
     * pstsIndicatorColor 滑动指示器的颜色
     * pstsUnderlineColor 整个 view【PagerSlidingTabStrip】下划线的颜色
     * pstsDividerColor tabs 之间分割线的颜色
     * pstsIndicatorHeight 滑动指示器的高度
     * pstsUnderlineHeight 整个 View【PagerSlidingTabStrip】下滑线的高度
     * pstsDivviderPadding 分割线上部、下部的内间距
     * pstsTabPaddingLeftRight 每个 tab 左右内间距
     * pstsScrollOffset 选中 tab 的滑动的距离
     * pstsTabBackground 每个 tab 的背景图片，使用 StateListDrawable
     * pstsShouldExpand 如果设置为 true，每个 tab 的宽度拥有相同的权重
     * pstsTextAllCaps 如果设置为 true，所有的 tab 字体转为大写
     */
    private void setTabls(PagerSlidingTabStrip tabs) {
        tabs.setIndicatorColorResource(R.color.colorPrimaryDark);  //滑动指示器颜色
        tabs.setUnderlineColorResource(R.color.colorAccent);  //整个 view【PagerSlidingTabStrip】下划线的颜色
        tabs.setDividerColorResource(R.color.colorAccent);  //tabs 之间分割线的颜色
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] strings;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            strings = new String[listColumn.size()];
            for(int i=0;i<listColumn.size();i++){
                strings[i] = listColumn.get(i).getGradeName();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strings[position];
        }

        @Override
        public int getCount() {
            return strings.length;
        }


        @Override
        public Fragment getItem(int position) {
//            if(position == 0){
//                return QuickContactFragment.newInstance();
//            }else{
                return SuperAwesomeCardFragment.newInstance(listColumn.get(position));
//            }
        }
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_news_column;
    }
}
