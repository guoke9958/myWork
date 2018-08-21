package com.cn.xa.qyw.ui.slide;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.UserAliAccount;
import com.cn.xa.qyw.utils.StringUtils;

/**
 * Created by Administrator on 2016/8/21.
 */
public class WithDrawDetailsActivity extends DoctorBaseActivity {

    private EditText mNumber;
    private TextView mBtnAdd;
    private int mCount;
    private UserAliAccount mItem;
    private TextView name;
    private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("提现至账户");
        mItem = (UserAliAccount) getIntent().getSerializableExtra("item");
        mCount = 10000;
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mNumber = (EditText) findViewById(R.id.withdrawals_number_edittext);
        mBtnAdd = (TextView) findViewById(R.id.next);
        name = (TextView)findViewById(R.id.name);
        number = (TextView)findViewById(R.id.number);
    }

    private void initListener() {
        mNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = mNumber.getText().toString().trim();

                if(!StringUtils.isEmpty(number)&&!"0".equals(number)){
                    int count = Integer.parseInt(number);

                    int yushu = count % 100;

                    if (yushu == 0 && count <= mCount) {
                        mBtnAdd.setEnabled(true);
                    } else {
                        mBtnAdd.setEnabled(false);
                    }
                }

            }
        });

        mBtnAdd.setOnClickListener(this);
    }

    private void initData() {
        String nameStr = mItem.getAliName().substring(1, mItem.getAliName().length());
        name.setText("*"+nameStr);
        number.setText(mItem.getAliAccount());
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_withdrawals_details;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String number = mNumber.getText().toString().trim();
        Intent intent = getNewIntent(WithDrawResultActivity.class);
        intent.putExtra("count",Integer.parseInt(number));
        intent.putExtra("item",mItem);
        startActivity(intent);
    }
}
