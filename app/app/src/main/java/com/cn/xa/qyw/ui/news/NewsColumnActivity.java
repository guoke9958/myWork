package com.cn.xa.qyw.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.ui.news.fragment.QuickContactFragment;
import com.cn.xa.qyw.ui.news.fragment.SuperAwesomeCardFragment;
import com.cn.xa.qyw.view.PagerSlidingTabStrip;

/**
 * 根据栏目来显示新闻资讯
 */
public class NewsColumnActivity extends DoctorBaseActivity {


    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private String mGrade;
    private int mGradeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGrade = getIntent().getStringExtra("grade");
        mGradeId = getIntent().getIntExtra("grade_id",0);

        mToolbarTitle.setText(mGrade);
        initView();
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

        private final String[] TITLES = { "简介", "视频", "文章", "案例", "自定义栏目一", "自定义栏目二",
                "自定义栏目三", "自定义栏目四" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return QuickContactFragment.newInstance();
            }else{
                return SuperAwesomeCardFragment.newInstance(position);
            }
        }

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_news_column;
    }
}
