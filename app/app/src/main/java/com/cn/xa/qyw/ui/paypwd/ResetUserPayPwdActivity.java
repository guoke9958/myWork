package com.cn.xa.qyw.ui.paypwd;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.UserPayPwd;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.SHA1;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/16.
 */
public class ResetUserPayPwdActivity extends DoctorBaseActivity {

    private TextView mTipTitle;
    private TextView mPhoneNumber;
    private Button mBtnNext;
    private EditText mEditVerifyCode;
    private Button mBtnGetVerifyCode;
    private DialogWidget mDialogWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("重置支付密码");
        initView();
        initListener();
        getAuthCode();
    }

    private void getAuthCode() {
        mBtnGetVerifyCode.setEnabled(false);
        showDialog();
        final String phone = DoctorApplication.mUser.getUserName();

        RequestParams rp = new RequestParams();
        rp.put("data", phone);
        rp.put("type", "forgetpwd");

        HttpUtils.postDataFromServer(HttpAddress.GET_AUTH_CODE, rp, new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                mTipTitle.setText("短信验证码发送失败，请重试！");
                mBtnGetVerifyCode.setEnabled(true);
                ToastUtils.showShortSnackbar(mToolbar, error);
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                mTipTitle.setText("我们已发送验证码到您的手机");
                retrySendVersionCode();
                ToastUtils.showShortSnackbar(mToolbar, "验证码发送成功");
            }
        });
    }

    private void initView() {
        mTipTitle = (TextView) findViewById(R.id.tip_title);
        mPhoneNumber = (TextView) findViewById(R.id.phone_number);
        mEditVerifyCode = (EditText) findViewById(R.id.verify_code);
        mBtnGetVerifyCode = (Button) findViewById(R.id.get_number_code);
        mBtnNext = (Button) findViewById(R.id.next_btn);

        mTipTitle.setText("正在发送短信中...");
        mPhoneNumber.setText(DoctorApplication.mUser.getUserName());
    }

    private void initListener() {
        mBtnGetVerifyCode.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBtnGetVerifyCode.getId()) {
            getAuthCode();
        } else if (id == mBtnNext.getId()) {
            sendRegisterInfo();
        }
    }

    private void sendRegisterInfo() {
        final String phone = DoctorApplication.mUser.getUserName();
        String verifyCode = mEditVerifyCode.getText().toString().trim();


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
                dismissDialog();
                mDialogWidget = new DialogWidget(ResetUserPayPwdActivity.this, getDecorViewDialog());
                mDialogWidget.show();
            }
        });

    }

    protected View getDecorViewDialog() {

        // TODO Auto-generated method stub
        return PayPasswordView.getInstance("", "重置支付密码", false, "", this, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                mDialogWidget.dismiss();
                mDialogWidget = null;
                addPayPwd(password);
            }

            @Override
            public void onCancelPay() {
                mDialogWidget.dismiss();
                mDialogWidget = null;
            }
        }).getView();
    }

    private void addPayPwd(String pwd) {
        showDialog();

        UserPayPwd payPwd = new UserPayPwd();
        payPwd.setUserId(DoctorApplication.mUser.getUserId());
        payPwd.setPayPwd(SHA1.encryptToSHA(pwd));
        payPwd.setUpdateTime(DateUtils.getCurrentTimestamp());

        HttpUtils.postDataFromServer(HttpAddress.ADD_AND_UPDATE_USER_PAY_PWD, JSONObject.toJSONString(payPwd), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                showToast("添加支付密码失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();

                if (count != 0) {
                    MaterialDialog dialog = new MaterialDialog.Builder(ResetUserPayPwdActivity.this)
                            .theme(Theme.LIGHT)
                            .title("温馨提示")
                            .content("重置支付密码成功！")
                            .show();

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
                }

            }
        });
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

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_reset_pay_pwd;
    }
}
