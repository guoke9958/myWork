package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.AboutAdapter;

/**
 * Created by Administrator on 2016/7/26.
 */
public class AboutDoctorAppActivity extends SlideBaseActivity {

    private ListView mListView;
    private AboutAdapter mAdapter;
    private TextView mTvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        mAdapter = new AboutAdapter(this);
        mListView.setAdapter(mAdapter);
        mTvVersionName.setText("新医患通"+getAppInfo());
    }

    private void initListener() {

    }

    private void initView() {
        mListView = (ListView)findViewById(R.id.about_list_view);
        mTvVersionName = (TextView)findViewById(R.id.version_name);
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_about_app;
    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }
}
