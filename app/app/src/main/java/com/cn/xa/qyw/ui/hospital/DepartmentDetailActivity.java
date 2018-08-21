package com.cn.xa.qyw.ui.hospital;

import android.os.Bundle;

import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by Administrator on 2016/7/26.
 */
public class DepartmentDetailActivity extends DoctorBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("科室详情");
    }

    @Override
    public int getChildLayoutId() {
        return 0;
    }
}
