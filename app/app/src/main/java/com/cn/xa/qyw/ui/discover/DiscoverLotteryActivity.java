package com.cn.xa.qyw.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cn.xa.qyw.R;

/**
 * 活动抽奖
 * Created by 409160 on 2016/7/8.
 */
public class DiscoverLotteryActivity extends DiscoverBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title",getIntent().getStringExtra("title"));
                intent.setClass(DiscoverLotteryActivity.this,DiscoverLotteryDrawActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_discover_lottery;
    }


}
