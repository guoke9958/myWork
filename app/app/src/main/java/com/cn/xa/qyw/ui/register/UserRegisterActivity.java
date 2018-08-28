package com.cn.xa.qyw.ui.register;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.paypwd.DialogWidget;
import com.cn.xa.qyw.ui.paypwd.ResetUserPayPwdActivity;
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
public class UserRegisterActivity extends DoctorBaseActivity {

    private EditText mEditVerifyCode;
    private Button mBtnGetVerifyCode;
    private EditText mEditUserName;
    private EditText mEditUserPwd;
    private ImageView mBtnSeePwd;
    private Button mBtnNext;
    private boolean isShowPassword;
    private int mPosition;
    private EditText mEditInvitePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("注 册");
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
        mEditInvitePhone = (EditText)findViewById(R.id.invite_phone);
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == mBtnNext.getId()) {
            showSelectUserType();
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

    private void showSelectUserType() {

        mPosition = 1;

        new MaterialDialog.Builder(this)
                .title("选择会员类型")
                .items(R.array.user_type)
                .itemsCallbackSingleChoice(1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        mPosition = which + 1;
                        checkAuthCode();
                        return true;
                    }
                })
                .positiveText("提交")
                .show();

    }

    private void checkAuthCode(){
        final String userName = mEditUserName.getText().toString().trim();
        final String userPwd = mEditUserPwd.getText().toString().trim();
        String verifyCode = mEditVerifyCode.getText().toString().trim();
        final String invitePhone = mEditInvitePhone.getText().toString().trim();
        if (!StringUtils.isTelActive(userName)) {
            showToast("注册手机号格式错误");
            return;
        }

        if(!StringUtils.isEmpty(invitePhone)){
            if (!StringUtils.isTelActive(invitePhone)) {
                showToast("邀请人手机号格式错误");
                return;
            }
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
                sendRegisterInfo(userName,userPwd,invitePhone);
            }
        });

    }

    private void sendRegisterInfo(String userName,String userPwd,String invitePhone) {

        User user = new User();
        user.setId(0);
        user.setType(mPosition);
        user.setUserName(userName);
        user.setPassword(MD5Util.encryptMD5(userPwd));
        user.setInvitePhone(invitePhone);

        HttpUtils.postDataFromServer(HttpAddress.USER_ADD, JSONObject.toJSONString(user), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                ToastUtils.showShortSnackbar(mToolbar, "注册失败");
            }

            @Override
            public void onSuccess(String data) {
                User user = JSONObject.parseObject(data, User.class);
                ToastUtils.showShortSnackbar(mToolbar, "注册成功");
                RongYunUtils.connect(UserRegisterActivity.this, user);
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
            HttpUtils.postDataFromServer(HttpAddress.GET_AUTH_CODE, phone, new NetworkResponseHandler() {
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
