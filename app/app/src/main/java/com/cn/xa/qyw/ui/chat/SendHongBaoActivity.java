package com.cn.xa.qyw.ui.chat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.CapitalHistory;
import com.cn.xa.qyw.entiy.SelectGodItem;
import com.cn.xa.qyw.entiy.UserPayPwd;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.paypwd.DialogWidget;
import com.cn.xa.qyw.ui.paypwd.PayPasswordView;
import com.cn.xa.qyw.ui.paypwd.ResetUserPayPwdActivity;
import com.cn.xa.qyw.ui.slide.SelectPriceActivity;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.SHA1;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/9/1.
 */
public class SendHongBaoActivity extends DoctorBaseActivity {

    private String mToUserId;
    private EditText mLiuYan;
    private Button mBtnSend;
    private TextView showTvCount;
    private EditText mEditCount;
    private DialogWidget mDialogWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("打赏");
        mToUserId = getIntent().getStringExtra("user_id");
        initView();
        initListener();
        getUserPayPwd(0);
    }

    private void initListener() {
        mEditCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String hongBao = mEditCount.getText().toString().trim();

                if (!StringUtils.isEmpty(hongBao)) {
                    int hongbaoCount = Integer.parseInt(hongBao);

                    if (hongbaoCount > 0) {
                        mBtnSend.setEnabled(true);
                    } else {
                        mBtnSend.setEnabled(false);
                    }

                    showTvCount.setText(hongBao + " 元");

                } else {
                    showTvCount.setText("0 元");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hongBao = mEditCount.getText().toString().trim();

                int hongbaoCount = Integer.parseInt(hongBao);
                showDialog();
                getUserPayPwd(hongbaoCount);
            }
        });
    }

    private void getUserPayPwd(final int hongbaoCount) {
        HttpUtils.postDataFromServer(HttpAddress.GET_USER_PAY_PWD, DoctorApplication.mUser.getUserId(), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                if ("不存在".equals(message)) {
                    mDialogWidget = new DialogWidget(SendHongBaoActivity.this, getDecorViewDialog(hongbaoCount));
                    mDialogWidget.show();
                } else {
                    showToast("服务器连接异常");
                }
            }

            @Override
            public void onSuccess(String data) {

                if (hongbaoCount != 0) {
                    dismissDialog();
                    mDialogWidget = new DialogWidget(SendHongBaoActivity.this, getPayDecorViewDialog(hongbaoCount));
                    mDialogWidget.show();

                }

            }
        });

    }

    protected View getDecorViewDialog(final int count) {

        // TODO Auto-generated method stub
        return PayPasswordView.getInstance("", "请先设置支付密码", true, "*密码可打赏，提现等资金操作，请您牢记", this, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                mDialogWidget.dismiss();
                mDialogWidget = null;
                addPayPwd(password, count);
            }

            @Override
            public void onCancelPay() {
                mDialogWidget.dismiss();
                mDialogWidget = null;
            }
        }).getView();
    }

    private void addPayPwd(String pwd, final int count) {
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
                    MaterialDialog dialog = new MaterialDialog.Builder(SendHongBaoActivity.this)
                            .theme(Theme.LIGHT)
                            .title("温馨提示")
                            .content("支付密码设置成功，请重新打赏！")
                            .positiveText("确定")
                            .show();
                }

            }
        });
    }


    private void initView() {
        mLiuYan = (EditText) findViewById(R.id.liuyan1);
        mBtnSend = (Button) findViewById(R.id.send_hongbao);
        showTvCount = (TextView) findViewById(R.id.dianquan_count);
        mEditCount = (EditText) findViewById(R.id.hongbao_count);
    }

    private void onClickSend(final int count) {

        final String uuid = UUID.randomUUID().toString().replace("-", "");

        CapitalHistory history = new CapitalHistory();
        history.setOrderId(uuid);
        history.setId(0);
        history.setCapitalType(2);
        history.setUserId(DoctorApplication.mUser.getUserId());
        history.setUpdateTime(DateUtils.getCurrentTimestamp());
        history.setPayType("hongbao");
        history.setChange(count);
        history.setToUserId(mToUserId);

        HttpUtils.postDataFromServer(HttpAddress.SEND_HONGBAO, JSONObject.toJSONString(history), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                ToastUtils.showShortSnackbar(mToolbar, "打赏失败,资金不足");

            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                String title = mLiuYan.getText().toString().trim();
                if (StringUtils.isEmpty(title)) {
                    title = "谢谢医生！";
                }


                Intent intent = new Intent();
                intent.putExtra("count", count);
                intent.putExtra("title", title);
                intent.putExtra("orderId", uuid);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    protected View getPayDecorViewDialog(final int count) {

        // TODO Auto-generated method stub
        return PayPasswordView.getInstance("￥ " + count + ".0", "请输入支付密码", true, "给医生打赏", this, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                mDialogWidget.dismiss();
                mDialogWidget = null;
                checkUserPayPwd(count, password);
            }

            @Override
            public void onCancelPay() {
                mDialogWidget.dismiss();
                mDialogWidget = null;
            }
        }).getView();
    }

    private void checkUserPayPwd(final int count, final String password) {
        showDialog();

        UserPayPwd payPwd = new UserPayPwd();
        payPwd.setUserId(DoctorApplication.mUser.getUserId());
        payPwd.setPayPwd(SHA1.encryptToSHA(password));

        HttpUtils.postDataFromServer(HttpAddress.CHECK_USER_PAY_PWD, JSONObject.toJSONString(payPwd), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();

                if (StringUtils.isEmpty(message)) {
                    showToast("服务器异常");
                } else {
                    if (message.contains("日")) {

                        if (message.contains("支付密码已被锁定")) {

                            MaterialDialog dialog = new MaterialDialog.Builder(SendHongBaoActivity.this)
                                    .theme(Theme.LIGHT)
                                    .title("异常提示")
                                    .content(message)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            startActivity(getNewIntent(ResetUserPayPwdActivity.class));
                                        }
                                    })
                                    .positiveText("找回密码")
                                    .negativeText("取消")
                                    .show();


                        } else {

                            MaterialDialog dialog = new MaterialDialog.Builder(SendHongBaoActivity.this)
                                    .theme(Theme.LIGHT)
                                    .title("异常提示")
                                    .content(message)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            startActivity(getNewIntent(ResetUserPayPwdActivity.class));
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            mDialogWidget = new DialogWidget(SendHongBaoActivity.this, getPayDecorViewDialog(count));
                                            mDialogWidget.show();
                                        }
                                    })
                                    .positiveText("找回密码")
                                    .negativeText("重新输入")
                                    .show();


                        }

                    } else {
                        showToast("连接异常");
                    }
                }
            }

            @Override
            public void onSuccess(String data) {
                onClickSend(count);
            }
        });


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();

    }


    @Override
    public int getChildLayoutId() {
        return R.layout.activity_send_hongbao;
    }
}
