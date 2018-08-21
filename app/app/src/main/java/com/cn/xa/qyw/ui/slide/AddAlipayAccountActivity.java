package com.cn.xa.qyw.ui.slide;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.UserAliAccount;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/21.
 */
public class AddAlipayAccountActivity extends DoctorBaseActivity implements TextWatcher {


    private EditText name_edittext;
    private EditText alipay_edittext;
    private TextView mBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("添加支付宝账号");
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        name_edittext.addTextChangedListener(this);
        alipay_edittext.addTextChangedListener(this);
        mBtnAdd.setOnClickListener(this);
    }

    private void initView() {
        name_edittext = (EditText) findViewById(R.id.name_edittext);
        alipay_edittext = (EditText) findViewById(R.id.alipay_edittext);
        mBtnAdd = (TextView) findViewById(R.id.next);
    }

    private void initData() {

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_add_alipay;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String name = name_edittext.getText().toString().trim();
        String alipay = alipay_edittext.getText().toString().trim();

        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(alipay)&&(StringUtils.isTelActive(alipay)||StringUtils.isEmailActive(alipay))) {
            mBtnAdd.setEnabled(true);
        } else {
            mBtnAdd.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == mBtnAdd.getId()){
            String name = name_edittext.getText().toString().trim();
            String alipay = alipay_edittext.getText().toString().trim();

            UserAliAccount aliAccount = new UserAliAccount();
            aliAccount.setUserId(DoctorApplication.mUser.getUserId());
            aliAccount.setAliAccount(alipay);
            aliAccount.setAliName(name);
            aliAccount.setCreateTime(DateUtils.getCurrentTimestamp());

            showDialog();
            HttpUtils.postDataFromServer(HttpAddress.ADD_USER_ALI_ACCOUNT, JSONObject.toJSONString(aliAccount), new NetworkResponseHandler() {
                @Override
                public void onFail(String message) {
                    dismissDialog();
                    showToast(message);
                }

                @Override
                public void onSuccess(String data) {
                    dismissDialog();
                    showToast("添加成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });


        }


    }
}
