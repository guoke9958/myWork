package com.cn.xa.qyw.ui.discover;

import android.app.Activity;
import android.os.Bundle;

import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by 409160 on 2016/7/8.
 */
public abstract class DiscoverBaseActivity extends DoctorBaseActivity {

    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra("title");
        mToolbarTitle.setText(name);
        mActivity = this;
    }
}
