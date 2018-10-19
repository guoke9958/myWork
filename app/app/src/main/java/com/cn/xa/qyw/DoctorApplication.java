package com.cn.xa.qyw;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.city.CityTotal;
import com.cn.xa.qyw.city.SelectCity;
import com.cn.xa.qyw.entiy.Push;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.factory.ListViewLoadViewFactory;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.meesage.TextImageMessage;
import com.cn.xa.qyw.meesage.TextImageMessageProvider;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.chat.CustomMessage;
import com.cn.xa.qyw.ui.chat.CustomMessageItemProvider;
import com.cn.xa.qyw.ui.chat.HongBaoMessage;
import com.cn.xa.qyw.ui.chat.HongBaoMessageProvider;
import com.cn.xa.qyw.utils.FileUtils;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.NetUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.shizhefei.mvc.MVCHelper;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;


/**
 * Created by 409160 on 2016/5/20.
 */
public class DoctorApplication extends MultiDexApplication {

    private static DoctorApplication instance;
    public static User mUser;
    public List<Activity> listActivity = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        CrashHandler.getInstance().init(getApplicationContext());
        CityTotal.initData(getApplicationContext());
        FileUtils.initAppFolder(getApplicationContext(),"com.cn.xa.qyw");
        MVCHelper.setLoadViewFractory(new ListViewLoadViewFactory());
        NetUtils.getInstance(getApplicationContext());
        initRongYun();
        PreferenceUtils.getInstance().init(getApplicationContext());
        SelectCity.getInstance().initProvinceDatas(getApplicationContext());
        Fresco.initialize(getApplicationContext());

        XGPushManager
                .setNotifactionCallback(new XGPushNotifactionCallback() {

                    @Override
                    public void handleNotify(XGNotifaction xGNotifaction) {
                        Lg.e( "处理信鸽通知：" + xGNotifaction);
                        // 获取标签、内容、自定义内容
                        String title = xGNotifaction.getTitle();
                        String content = xGNotifaction.getContent();
                        String customContent = xGNotifaction
                                .getCustomContent();
                        // 其它的处理
                        // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。

                        if(isBackground(getApplicationContext())){
                            xGNotifaction.doNotify();
                        }else{
                            String userName = PreferenceUtils.getPrefString(PreferenceKeys.USER_ID, "");

                            if(StringUtils.isEmpty(userName)){
                                xGNotifaction.doNotify();
                            }else{

                                Lg.e("user已登录");
                                Push push = new Push();
                                push.setUserId(userName);
                                push.setContent(content);
                                HttpUtils.postDataFromServer(HttpAddress.PUSH_SINGLE_USER, com.alibaba.fastjson.JSONObject.toJSONString(push),
                                        new NetworkResponseHandler() {
                                    @Override
                                    public void onFail(String message) {

                                    }

                                    @Override
                                    public void onSuccess(String data) {
                                        Lg.e("已通知本地服务器");
                                    }
                                });

                            }
                        }



                    }
                });

    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }else{
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    public static DoctorApplication getInstance() {
        return instance;
    }

    private void initRongYun() {
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            try {
                RongIMClient.registerMessageType(HongBaoMessage.class);
                RongIM.getInstance().registerMessageTemplate(new HongBaoMessageProvider());

                RongIMClient.registerMessageType(CustomMessage.class);
                RongIM.getInstance().registerMessageTemplate(new CustomMessageItemProvider());

                RongIMClient.registerMessageType(TextImageMessage.class);
                RongIM.getInstance().registerMessageTemplate(new TextImageMessageProvider());

                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                    @Override
                    public UserInfo getUserInfo(final String userId) {
                        Lg.e("刷新用户信息");
                        return findUser(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                    }

                }, true);

            } catch (AnnotationNotFoundException e) {
                Lg.e(e);
            }

    }

    private UserInfo findUser(final String userId) {
        HttpUtils.postDataFromServer(HttpAddress.GET_USER_INFO, userId, new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {

            }

            @Override
            public void onSuccess(String data) {
                if (!StringUtils.isEmpty(data)) {
                    com.cn.xa.qyw.entiy.UserInfo mUser = JSONObject.parseObject(data, com.cn.xa.qyw.entiy.UserInfo.class);
                    if (mUser != null) {

                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId,
                                StringUtils.getUserName(mUser), Uri.parse(HttpAddress.PHOTO_URL + mUser.getPhoto())));
                    }
                }

            }
        });

        return null;

    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public void exist() {
        for (Activity activity : listActivity) {
            activity.finish();
        }
        mUser = null;
    }


}
