package com.cn.xa.qyw.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;

import io.rong.imkit.MainActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by 409160 on 2016/7/6.
 */
public class RongYunUtils {


    /**
     * 建立与融云服务器的连接
     *
     * @param user
     */
    public static void connect(final DoctorBaseActivity context, final User user, final String type) {

        if (context.getApplicationInfo().packageName.equals(DoctorApplication.getCurProcessName(context))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(user.getToken(), new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    DoctorApplication.mUser = user;
                    context.dismissDialog();
                    PreferenceUtils.setPrefString(PreferenceKeys.RONG_YUN_TOKEN, user.getToken());
                    PreferenceUtils.setPrefString(PreferenceKeys.USER_NAME, user.getUserName());
                    PreferenceUtils.setPrefString(PreferenceKeys.USER_PASSWORD, user.getPassword());
                    Lg.e("rongyun===" + userid);

                    Intent intent = new Intent();
                    intent.setAction("send_login");
                    context.sendBroadcast(intent);

                    if ("guide".equals(type)) {
                        context.startActivity(new Intent(context, DoctorMainActivity.class));
                    }

                    context.setResult(context.RESULT_OK);
                    context.finish();

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    context.dismissDialog();
                    Toast.makeText(context, "连接服务器失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void connect(final DoctorBaseActivity context, final User user) {

        if (context.getApplicationInfo().packageName.equals(DoctorApplication.getCurProcessName(context))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(user.getToken(), new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    DoctorApplication.mUser = user;

                    PreferenceUtils.setPrefString(PreferenceKeys.RONG_YUN_TOKEN, user.getToken());
                    PreferenceUtils.setPrefString(PreferenceKeys.USER_NAME, user.getUserName());
                    PreferenceUtils.setPrefString(PreferenceKeys.USER_PASSWORD, user.getPassword());
                    Lg.e("rongyun===" + userid);

                    Intent intent = new Intent();
                    intent.setAction("send_login");
                    context.sendBroadcast(intent);

                    if (DoctorApplication.mUser != null) {
                        if (DoctorMainActivity.mUserInfo == null) {
                            queryUserInfo(context);
                        }else{
                            context.dismissDialog();
                            context.setResult(context.RESULT_OK);
                            context.finish();
                        }
                    }

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    context.dismissDialog();
                    Toast.makeText(context, "连接服务器失败 " + errorCode.getValue(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void queryUserInfo(final DoctorBaseActivity context) {
        if (DoctorApplication.mUser != null) {
            String url = HttpAddress.GET_USER_INFO + "?data=" + DoctorApplication.mUser.getUserId();
            HttpUtils.postDataFromServer(url, new NetworkResponseHandler() {
                @Override
                public void onFail(String messsage) {

                }

                @Override
                public void onSuccess(String data) {
                    context.dismissDialog();
                    if (!StringUtils.isEmpty(data)) {
                        com.cn.xa.qyw.entiy.UserInfo mUserInfo = JSONObject.parseObject(data, com.cn.xa.qyw.entiy.UserInfo.class);

                        io.rong.imlib.model.UserInfo userInfo = new io.rong.imlib.model.UserInfo(mUserInfo.getUserId(), StringUtils.getUserName(mUserInfo), Uri.parse(HttpAddress.PHOTO_URL + mUserInfo.getPhoto()));
                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                        RongIM.getInstance().setMessageAttachedUserInfo(true);

                        DoctorMainActivity.mUserInfo = mUserInfo;

                    }

                    context.setResult(context.RESULT_OK);
                    context.finish();

                }
            });
        }

    }
}
