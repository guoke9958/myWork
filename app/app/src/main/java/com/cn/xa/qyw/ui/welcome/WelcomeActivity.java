package com.cn.xa.qyw.ui.welcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.dialog.DoctorBaseDialog;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.login.LoginActivity;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.ui.userinfo.ModifyUserIdActivity;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.NetUtils;
import com.cn.xa.qyw.utils.RongYunUtils;
import com.cn.xa.qyw.utils.StringUtils;

import io.rong.imkit.MainActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2016/7/3.
 */
public class WelcomeActivity extends DoctorBaseActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        boolean isNet = NetUtils.isNetConnected();

        if (!isNet) {
            DoctorBaseDialog.showNetErrorDialog(this);
            return;
        }

    }

    @Override
    public void initContentView() {
    }

    @Override
    public void initBaseView() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        String token = PreferenceUtils.getPrefString(PreferenceKeys.RONG_YUN_TOKEN, "");

        if (!StringUtils.isEmpty(token)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    login();
                }
            }, 2000);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(getNewIntent(DoctorMainActivity.class));
                    finish();
                }
            }, 5000);
        }
    }

    private void connectRongYun(final String token) {
        if (getApplicationInfo().packageName.equals(DoctorApplication.getCurProcessName(this))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Lg.e("token===过期");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    DoctorApplication.mUser = mUser;
                    PreferenceUtils.setPrefString(PreferenceKeys.RONG_YUN_TOKEN, token);
                    Intent intent = getNewIntent(DoctorMainActivity.class);
                    startActivity(intent);

                    finish();

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(WelcomeActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void login() {
        String userName = PreferenceUtils.getPrefString(PreferenceKeys.USER_NAME, "");
        String password = PreferenceUtils.getPrefString(PreferenceKeys.USER_PASSWORD, "");

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);

        HttpUtils.postDataFromServer(HttpAddress.USER_LOGIN, JSONObject.toJSONString(user), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                Lg.e("登陆失败");
                Intent intent = getNewIntent(DoctorMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onSuccess(String data) {
                mUser = JSONObject.parseObject(data, User.class);

                try {
                    long phone = Long.parseLong(mUser.getUserName());
                    connectRongYun(mUser.getToken());
                    Lg.e("登陆成功" + data);
                } catch (Exception e) {
                    Lg.e(e);
                    Intent intent = getNewIntent(ModifyUserIdActivity.class);
                    intent.putExtra("user", mUser);
                    startActivityForResult(intent, 101, null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                startActivity(getNewIntent(MainActivity.class));
                finish();
            }
        }

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initBaseViewData() {
    }
}
