package com.cn.xa.qyw.ui.userinfo;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.utils.MD5Util;
import com.cn.xa.qyw.utils.StringUtils;

/**
 * Created by 409160 on 2016/12/9.
 */
public class ModifyUserPwdActivity extends DoctorBaseActivity {

    private EditText mEditUserPwd;
    private ImageView mBtnSeePwd;
    private Button mBtnNext;
    private ImageView mRepeatBtnSeePwd;
    private EditText mRepeatEditUserPwd;
    private boolean isShowPassword;
    private boolean isShowRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("修改登录密码");
        initView();
        initListener();
    }

    private void initListener() {
        mBtnSeePwd.setOnClickListener(this);
        mRepeatBtnSeePwd.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    private void initView() {
        mEditUserPwd = (EditText) findViewById(R.id.password_editText);
        mBtnSeePwd = (ImageView) findViewById(R.id.show_password_checkbox);
        mRepeatEditUserPwd = (EditText) findViewById(R.id.password_editText1);
        mRepeatBtnSeePwd = (ImageView) findViewById(R.id.show_password_checkbox1);
        mBtnNext = (Button) findViewById(R.id.next_btn);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == mBtnNext.getId()) {
            sendModifyUserPwd();
        } else if (id == mBtnSeePwd.getId()) {
            isShowPassword = !isShowPassword;
            if (isShowPassword) {
                mBtnSeePwd.setImageResource(R.mipmap.password_show);
                mEditUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mBtnSeePwd.setImageResource(R.mipmap.password_hidden);
                mEditUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            }
        } else if (id == mRepeatBtnSeePwd.getId()) {
            isShowRepeatPassword = !isShowRepeatPassword;
            if (isShowRepeatPassword) {
                mRepeatBtnSeePwd.setImageResource(R.mipmap.password_show);
                mRepeatEditUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mRepeatBtnSeePwd.setImageResource(R.mipmap.password_hidden);
                mRepeatEditUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
    }

    private void sendModifyUserPwd() {
        final String pwd = mEditUserPwd.getText().toString().trim();
        String repeatPwd = mRepeatEditUserPwd.getText().toString().trim();

        if(!StringUtils.isCurrentPassword(pwd)){
            showToast("密码长度不符合要求");
        }else{
            if(!StringUtils.isEquals(pwd,repeatPwd)){
                showToast("2次输入密码不一致");
            }else{
                DoctorApplication.mUser.setPassword(MD5Util.encryptMD5(pwd));
                showDialog();
                HttpUtils.postDataFromServer(HttpAddress.UPDATE_USER_PWD, JSONObject.toJSONString(DoctorApplication.mUser), new NetworkResponseHandler() {
                    @Override
                    public void onFail(String message) {
                        dismissDialog();
                        showToast("修改密码失败");
                    }

                    @Override
                    public void onSuccess(String data) {
                        dismissDialog();
                        showToast("修改密码成功");
                        PreferenceUtils.setPrefString(PreferenceKeys.USER_PASSWORD, MD5Util.encryptMD5(pwd));
                        finish();
                    }
                });
            }
        }

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_modify_userpwd;
    }
}
