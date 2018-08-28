package com.cn.xa.qyw.ui.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.ui.register.UserRegisterActivity;
import com.cn.xa.qyw.ui.userinfo.ForgetPwdActivity;
import com.cn.xa.qyw.ui.userinfo.ModifyUserIdActivity;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.MD5Util;
import com.cn.xa.qyw.utils.RongYunUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;

/**
 * Created by 409160 on 2016/5/25.
 */
public class LoginActivity extends DoctorBaseActivity {

    private Button mBtnLogin, mBtnRegister;
    private EditText mEditUserName, mEditPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("登 录");
        initView();
        initListener();
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    private void initView() {
        mBtnLogin = (Button) findViewById(R.id.login_btn);
        mBtnRegister = (Button) findViewById(R.id.regist_text);
        mEditUserName = (EditText) findViewById(R.id.sEtUserName);
        mEditPassword = (EditText) findViewById(R.id.sEtPassword);
        View mForgetPwd = findViewById(R.id.sForgetPassword);
        mBtnLogin.setEnabled(true);

        mForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getNewIntent(ForgetPwdActivity.class));
            }
        });
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBtnLogin.getId()) {
            login();
        } else if (id == mBtnRegister.getId()) {
            startActivityForResult(getNewIntent(UserRegisterActivity.class), 100, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                finish();
            }if (requestCode == 101) {
                finish();
            }
        }
    }

    private void login() {
        showDialog();
        String userName = mEditUserName.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();

        User user = new User();
        user.setUserName(userName);

        if(StringUtils.isEmpty(password)){
            user.setPassword("");
        }else{
            user.setPassword(MD5Util.encryptMD5(password));
        }

        HttpUtils.postDataFromServer(HttpAddress.USER_LOGIN, JSONObject.toJSONString(user), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                Lg.e("登陆失败");
                dismissDialog();
                ToastUtils.showShortSnackbar(mToolbarTitle, "用户名或密码错误");
            }

            @Override
            public void onSuccess(String data) {
                User user = JSONObject.parseObject(data, User.class);

                int type = user.getType();
//
                try {
                    long phone = Long.parseLong(user.getUserName());
                    PreferenceUtils.setPrefString(PreferenceKeys.USER_ID, user.getUserId());
                    RongYunUtils.connect(LoginActivity.this, user);
                } catch (Exception e) {
                    Lg.e(e);
                    Intent intent = getNewIntent(ModifyUserIdActivity.class);
                    intent.putExtra("user",user);
                    startActivityForResult(intent,101,null);
                }

            }
        });
    }
}
