package com.cn.xa.qyw.ui.userinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.MD5Util;
import com.cn.xa.qyw.utils.RongYunUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ForgetPwdActivity extends DoctorBaseActivity {

    private EditText mEditVerifyCode;
    private Button mBtnGetVerifyCode;
    private EditText mEditUserName;
    private EditText mEditUserPwd;
    private ImageView mBtnSeePwd;
    private Button mBtnNext;
    private boolean isShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("密码找回");
        initView();
        initListener();
        initData();
    }

    private void initData() {

    }

    private void initListener() {
        mBtnGetVerifyCode.setOnClickListener(this);
        mBtnSeePwd.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    private void initView() {
        mEditVerifyCode = (EditText) findViewById(R.id.verify_code);
        mBtnGetVerifyCode = (Button) findViewById(R.id.get_number_code);
        mEditUserName = (EditText) findViewById(R.id.phone_number_edit);
        mEditUserPwd = (EditText) findViewById(R.id.password_editText);
        mBtnSeePwd = (ImageView) findViewById(R.id.show_password_checkbox);
        mBtnNext = (Button) findViewById(R.id.next_btn);
        mBtnNext.setText("立即找回");
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBtnNext.getId()) {

            checkAuthCode();
        } else if (id == mBtnGetVerifyCode.getId()) {

            getVerifyCode();
        } else if (id == mBtnSeePwd.getId()) {
            isShowPassword = !isShowPassword;
            if (isShowPassword) {
                mBtnSeePwd.setImageResource(R.mipmap.password_show);
                mEditUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                mBtnSeePwd.setImageResource(R.mipmap.password_hidden);
                mEditUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
    }


    private void checkAuthCode(){
        final String userName = mEditUserName.getText().toString().trim();
        final String userPwd = mEditUserPwd.getText().toString().trim();
        String verifyCode = mEditVerifyCode.getText().toString().trim();

        if (!StringUtils.isTelActive(userName)) {
            showToast("手机号格式错误");
            return;
        }

        if (StringUtils.isEmpty(verifyCode)) {
            showToast("请输入验证码");
            return;
        }

        if (!StringUtils.isCurrentPassword(userPwd)) {
            showToast("密码长度为6-16位");
            return;
        }

        showDialog();
        RequestParams rp = new RequestParams();
        rp.put("phone", userName);
        rp.put("code", verifyCode);

        HttpUtils.postDataFromServer(HttpAddress.CHECK_AUTH_CODE, rp, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                ToastUtils.showShortSnackbar(mToolbar, "验证码不正确");
            }

            @Override
            public void onSuccess(String data) {
                sendRegisterInfo(userName,userPwd);
            }
        });

    }



    private void sendRegisterInfo(String userName,String userPwd) {

            User user = new User();
            user.setId(0);
            user.setUserName(userName);
            user.setPassword(MD5Util.encryptMD5(userPwd));

            HttpUtils.postDataFromServer(HttpAddress.FORGET_USER_PWD, JSONObject.toJSONString(user), new NetworkResponseHandler() {
                @Override
                public void onFail(String error) {
                    dismissDialog();
                    ToastUtils.showShortSnackbar(mToolbar, "密码找回失败");
                }

                @Override
                public void onSuccess(String data) {
                    dismissDialog();
                    ToastUtils.showShortSnackbar(mToolbar, "密码找回成功，请使用新密码登陆");
                    finish();
                }
            });

    }

    private void getVerifyCode() {
        final String phone = mEditUserName.getText().toString().trim();

        if (!StringUtils.isTelActive(phone)) {
            ToastUtils.showShortSnackbar(mToolbar, "手机号格式不正确");
            return;
        }


        if (StringUtils.isTelActive(phone)) {
            mBtnGetVerifyCode.setEnabled(false);
            showDialog();

            RequestParams rp = new RequestParams();
            rp.put("data",phone);
            rp.put("type", "forgetpwd");
            HttpUtils.postDataFromServer(HttpAddress.GET_AUTH_CODE, rp, new NetworkResponseHandler() {
                @Override
                public void onFail(String error) {
                    dismissDialog();
                    mBtnGetVerifyCode.setEnabled(true);
                    ToastUtils.showShortSnackbar(mToolbar, error);
                }

                @Override
                public void onSuccess(String data) {
                    dismissDialog();
                    retrySendVersionCode();
                    ToastUtils.showShortSnackbar(mToolbar, "验证码发送成功");
                }
            });
        } else {
            ToastUtils.showShortSnackbar(mToolbarTitle, "手机号格式不正确");
        }
    }


    private void retrySendVersionCode() {
        count = 1;
        timer = new Timer();

        task = new TimerTask() {

            @Override
            public void run() {
                // 需要做的事:发送消息
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
    }

    private int count;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                if (count > 60) {
                    timer.cancel();
                    task.cancel();
                    timer = null;
                    task = null;
                    mBtnGetVerifyCode.setEnabled(true);
                    mBtnGetVerifyCode.setBackgroundResource(R.mipmap.click_get_verify_code);
                    mBtnGetVerifyCode.setText("");
                } else {
                    mBtnGetVerifyCode.setEnabled(false);
                    mBtnGetVerifyCode.setBackgroundResource(R.mipmap.verify_code_time);
                    String content = "重新发送";
                    if (count < 10) {
                        content = "0" + count;
                    } else {
                        content = "" + count;
                    }

                    count++;

                    mBtnGetVerifyCode.setText(content);
                }

            }
            super.handleMessage(msg);
        }

        ;
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

}
