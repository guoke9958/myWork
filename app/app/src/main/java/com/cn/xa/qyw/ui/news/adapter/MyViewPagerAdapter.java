package com.cn.xa.qyw.ui.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.ui.news.fragment.QuickContactFragment;
import com.cn.xa.qyw.ui.news.fragment.SuperAwesomeCardFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * viewPager + fragment 显示渲染器
 * ViewPager的适配器，用于根据List的tab名来生成对应的Fragment，每个Fragment必须传入其type的参数
 * Created by amoldZhang on 2016/12/13.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<HospitalGrade> mTabNameList;
    private Fragment mCurFragment;

    public   MyViewPagerAdapter(FragmentManager fm, List<HospitalGrade> categoryList) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mTabNameList = categoryList;
        for (HospitalGrade hospitalGrade : categoryList) {
            Fragment f = new SuperAwesomeCardFragment().setArguments(hospitalGrade);
//            Fragment f = new QuickContactFragment().setArguments(hospitalGrade);
            mFragmentList.add(f);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList != null ? mFragmentList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabNameList.get(position).getGradeName();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurFragment = (Fragment) object;
    }

    Fragment getCurrentFragment() {
        return mCurFragment;
    }
}
