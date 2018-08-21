package com.cn.xa.qyw.ui.hospital;

import android.os.Bundle;

import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by 409160 on 2016/7/7.
 */
public class HospitalDetailActivity extends DoctorBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String hospitalName = getIntent().getStringExtra("hospital_name");
        mToolbarTitle.setText(hospitalName);
    }

    @Override
    public int getChildLayoutId() {
        return 0;
    }
}
