package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;

import com.cn.xa.qyw.base.DoctorBaseActivity;

/**
 * Created by Administrator on 2016/7/26.
 */
public abstract class SlideBaseActivity extends DoctorBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int title = getIntent().getIntExtra("title",0);

        if(title!=0){
            mToolbarTitle.setText(title);
        }

    }
}
