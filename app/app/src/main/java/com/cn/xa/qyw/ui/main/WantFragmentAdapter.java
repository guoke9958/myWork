package com.cn.xa.qyw.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



import java.util.ArrayList;

/**
 * Created by cn on 15-6-2.
 */
public class WantFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;


    public WantFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);

        fragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
