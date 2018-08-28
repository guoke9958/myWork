package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by 409160 on 2017/2/14.
 */
public class PushServiceGuiZeActivity extends DoctorBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("推送须知");
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_push_service;
    }
}
