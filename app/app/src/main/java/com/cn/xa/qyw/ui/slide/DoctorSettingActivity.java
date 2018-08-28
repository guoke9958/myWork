package com.cn.xa.qyw.ui.slide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.ui.welcome.WelcomeActivity;
import com.cn.xa.qyw.utils.RongYunUtils;
import com.cn.xa.qyw.utils.StringUtils;

import java.io.File;

import io.rong.imkit.MainActivity;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/7/26.
 */
public class DoctorSettingActivity extends SlideBaseActivity {

    private RelativeLayout mNewMessage;
    private RelativeLayout mClean;
    private RelativeLayout mExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("设置");
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        mNewMessage.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }

    private void initView() {
        mNewMessage = (RelativeLayout) findViewById(R.id.ac_set_new_message);
        mClean = (RelativeLayout) findViewById(R.id.ac_set_clean);
        mExit = (RelativeLayout) findViewById(R.id.ac_set_exit);

       String token = PreferenceUtils.getPrefString(PreferenceKeys.RONG_YUN_TOKEN, "");
        if(!StringUtils.isEmpty(token)){
            mExit.setVisibility(View.VISIBLE);
        }else{
            mExit.setVisibility(View.GONE);
        }
    }

    private void initData() {

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_user_setting;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_set_new_message:
                break;
            case R.id.ac_set_clean:

                break;
            case R.id.ac_set_exit:

                PreferenceUtils.setPrefString(PreferenceKeys.RONG_YUN_TOKEN, "");
                PreferenceUtils.setPrefString(PreferenceKeys.USER_NAME, "");
                PreferenceUtils.setPrefString(PreferenceKeys.USER_PASSWORD, "");
                RongIM.getInstance().logout();
                DoctorMainActivity.mUserInfo = null;
                DoctorApplication.mUser = null;
                Intent intent = new Intent();
                intent.setAction("send_logout");
                sendBroadcast(intent);
                finish();
                break;
        }
    }



    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

}
