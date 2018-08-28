package com.cn.xa.qyw.ui.userinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.paypwd.DialogWidget;
import com.cn.xa.qyw.ui.paypwd.ResetUserPayPwdActivity;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.RongYunUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 409160 on 2016/7/6.
 */
public class ModifyUserIdActivity extends DoctorBaseActivity {

    private User mUser;
    private Button mBtnAuthCode;
    private EditText mEditUserName, mEditAuthCode;
    private Button mBtnSend;
    private View mTip;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getIntent().getSerializableExtra("user");
        mType = getIntent().getStringExtra("type");
        mToolbarTitle.setText("修改登陆账号");
        initView();
        if ("modif".equals(mUser.getRequestType())) {
            btnBack.setVisibility(View.VISIBLE);
            mTip.setVisibility(View.GONE);
        } else {
            btnBack.setVisibility(View.GONE);
            mTip.setVisibility(View.VISIBLE);
        }


        initListener();
    }

    private void initListener() {
        mBtnAuthCode.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
    }

    private void initView() {
        mBtnAuthCode = (Button) findViewById(R.id.get_number_code);
        mEditUserName = (EditText) findViewById(R.id.phone_number_edit);
        mEditAuthCode = (EditText) findViewById(R.id.verify_code);
        mBtnSend = (Button) findViewById(R.id.next_btn);
        mTip = findViewById(R.id.tip);
        View mNext = findViewById(R.id.next);
        if ("modif".equals(mUser.getRequestType())) {
            mNext.setVisibility(View.GONE);
        } else {
            mNext.setVisibility(View.VISIBLE);
        }

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_modify_userid;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.get_number_code) {
            getAuthCode();
        } else if (id == R.id.next_btn) {
            sendRegisterInfo();
        }
    }

    private void sendRegisterInfo() {
        String verifyCode = mEditAuthCode.getText().toString().trim();
        final String phone = mEditUserName.getText().toString().trim();

        if (DoctorApplication.mUser != null && DoctorApplication.mUser.getUserName().equals(phone)) {
            showToast("和现有登陆名一样，无需更换");
            return;
        }

        if (!StringUtils.isTelActive(phone)) {
            showToast("手机号格式错误");
            return;
        }

        if (StringUtils.isEmpty(verifyCode)) {
            ToastUtils.showShortSnackbar(mToolbar, "请输入验证码");
            return;
        }

        showDialog();

        RequestParams rp = new RequestParams();
        rp.put("phone", phone);
        rp.put("code", verifyCode);

        HttpUtils.postDataFromServer(HttpAddress.CHECK_AUTH_CODE, rp, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                ToastUtils.showShortSnackbar(mToolbar, "验证码不正确");
            }

            @Override
            public void onSuccess(String data) {
                modifyUserId(phone);
            }
        });

    }

    private void modifyUserId(String phone) {

        mUser.setUserName(phone);
        HttpUtils.postDataFromServer(HttpAddress.USER_MODIFY_USERID, JSONObject.toJSONString(mUser), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                ToastUtils.showShortSnackbar(mToolbarTitle, "修改用户信息失败");
            }

            @Override
            public void onSuccess(String data) {
                login();
            }
        });


    }

    private void login() {
        HttpUtils.postDataFromServer(HttpAddress.USER_LOGIN, JSONObject.toJSONString(mUser), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                Lg.e("登陆失败");
                dismissDialog();
                ToastUtils.showShortSnackbar(mToolbarTitle, "用户名或密码错误");
            }

            @Override
            public void onSuccess(String data) {
                User user = JSONObject.parseObject(data, User.class);

                if (StringUtils.isEmpty(user.getToken())) {
                    dismissDialog();
                    ToastUtils.showShortSnackbar(mToolbar, "聊天服务器登陆失败");
                } else {
                    if ("modif".equals(mUser.getRequestType())) {
                        dismissDialog();
                        PreferenceUtils.setPrefString(PreferenceKeys.USER_NAME, mUser.getUserName());
                        DoctorApplication.mUser.setUserName(mUser.getUserName());
                        finish();
                    } else {
                        RongYunUtils.connect(ModifyUserIdActivity.this, user, mType);
                    }
                }

                Lg.e("登陆成功" + data);
            }
        });
    }

    private void getAuthCode() {
        final String phone = mEditUserName.getText().toString().trim();

        if (DoctorApplication.mUser != null && DoctorApplication.mUser.getUserName().equals(phone)) {
            showToast("和现有登陆名一样，请重新输入");
            return;
        }

        if (StringUtils.isTelActive(phone)) {
            mBtnAuthCode.setEnabled(false);
            showDialog();
            HttpUtils.postDataFromServer(HttpAddress.GET_AUTH_CODE, phone, new NetworkResponseHandler() {
                @Override
                public void onFail(String error) {
                    dismissDialog();
                    mBtnAuthCode.setEnabled(true);
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
                    mBtnAuthCode.setEnabled(true);
                    mBtnAuthCode.setBackgroundResource(R.mipmap.click_get_verify_code);
                    mBtnAuthCode.setText("");
                } else {
                    mBtnAuthCode.setEnabled(false);
                    mBtnAuthCode.setBackgroundResource(R.mipmap.verify_code_time);
                    String content = "重新发送";
                    if (count < 10) {
                        content = "0" + count;
                    } else {
                        content = "" + count;
                    }

                    count++;

                    mBtnAuthCode.setText(content);
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


    @Override
    public void onBackPressed() {
        if ("modif".equals(mUser.getRequestType())) {
            super.onBackPressed();
        } else {
            btnBack.setVisibility(View.GONE);
        }
    }
}
