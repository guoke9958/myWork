package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by Administrator on 2016/10/31.
 */
public class AccountXuZhiActivity extends DoctorBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("会员须知");
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_account_xuzhi;
    }
}
