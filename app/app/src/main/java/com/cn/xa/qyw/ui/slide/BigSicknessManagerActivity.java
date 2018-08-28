package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;

import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by Administrator on 2016/10/31.
 */
public class BigSicknessManagerActivity extends DoctorBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("大病会诊管理");
    }

    @Override
    public int getChildLayoutId() {
        return 0;
    }
}
