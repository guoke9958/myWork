package com.cn.xa.qyw.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.login.LoginActivity;
import com.cn.xa.qyw.utils.StringUtils;

/**
 * Created by 409160 on 2016/7/6.
 */
public class DoctorBaseDialog {

    public static boolean isShowLoginDialog(final Context context) {

        if (DoctorApplication.mUser == null) {

            new MaterialDialog.Builder(context)
                    .title("登录提示")
                    .content("您未登录应用，登录之后享受更多优质服务！")
                    .positiveText("登陆")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    })
                    .negativeText("取消")
                    .show();
            return true;
        }

        return false;
    }

    public static void showNetErrorDialog(final Context context) {

            new MaterialDialog.Builder(context)
                    .title("网络异常提示")
                    .content("请检查网络设置！")
                    .positiveText("去设置")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = null;
                            // 先判断当前系统版本
                            if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
                                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            }else{
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            context.startActivity(intent);

                        }
                    })
                    .negativeText("取消")
                    .show();
    }
}
