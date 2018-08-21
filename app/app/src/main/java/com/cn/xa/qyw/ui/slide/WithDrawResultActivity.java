package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.UserAliAccount;

/**
 * Created by Administrator on 2016/8/22.
 */
public class WithDrawResultActivity extends DoctorBaseActivity {

    private TextView withdrawls_ing;
    private TextView withdrawals_number_text;
    private TextView withdrawls_text;
    private UserAliAccount mItem;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("提现结果");
        mItem = (UserAliAccount) getIntent().getSerializableExtra("item");
        mCount = getIntent().getIntExtra("count", 0);
        initView();
        initListener();
        initData();

    }

    private void initView() {
        withdrawls_ing = (TextView) findViewById(R.id.withdrawls_ing);
        withdrawals_number_text = (TextView) findViewById(R.id.withdrawals_yuan_edittext);
        withdrawls_text = (TextView) findViewById(R.id.withdrawls_text);
    }

    private void initListener() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                withdrawls_ing.setText(R.string.withdrawals_request_success);
                closeActivity();
            }
        }, 2000);


    }

    private void closeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 10000);
    }

    private void initData() {
        withdrawls_text.setText(mItem.getAliAccount());
        int yushu = mCount / 100;
        withdrawals_number_text.setText(yushu + "元");
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_withdrawals_result;
    }
}
