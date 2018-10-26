package com.cn.xa.qyw.ui.slide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.AboutAdapter;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;

/**
 * 关于我们
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
        mTvVersionName.setText("医通百通"+getAppInfo());
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( 1 == (mAdapter.getItem(position).getType())){
//                      showToast("");
                }else if (2 == mAdapter.getItem(position).getType()){
//                    showToast("");
                }else if (3 == mAdapter.getItem(position).getType()){
//                    showToast("");
                }else if (4 == mAdapter.getItem(position).getType()){
                    Intent intent = new Intent();
                    intent.putExtra("title", R.string.add_friends);
                    intent.setClass(AboutDoctorAppActivity.this,AboutAddUsActivity.class );
                    startActivity(intent);
                }
            }
        });
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
