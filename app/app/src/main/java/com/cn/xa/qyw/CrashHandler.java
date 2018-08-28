package com.cn.xa.qyw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.entiy.AppCrash;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.welcome.WelcomeActivity;
import com.cn.xa.qyw.utils.DateUtils;

import io.rong.imageloader.utils.L;


public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 660:
                    restartApplication();
                    break;
            }

        }

        ;

    };

    private void restartApplication() {
        final Intent intent = mContext.getPackageManager()
                .getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                L.e("error : " + e.getMessage());
            }
        }
        DoctorApplication.getInstance().exist();
        restartApp();
    }

    public void restartApp() {
        Intent intent = new Intent(mContext, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.getMainLooper();
                Looper.prepare();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        // 重新启动smartHome

        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            infos.clear();
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            L.e("an error occured when collect package info" + e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                L.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                L.e(TAG,
                        "an error occured when collect crash info"
                                + e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();

        AppCrash crash = new AppCrash();
        if (DoctorApplication.mUser != null) {
            crash.setUserId(DoctorApplication.mUser.getUserId());
        }

        crash.setDeviceInfo(sb.toString());
        crash.setContent(result);
        crash.setCreateTime(DateUtils.getCurrentTimestamp());
        crash.setIsSolve(0);

        HttpUtils.postDataFromServer(HttpAddress.ADD_CRASH, JSONObject.toJSONString(crash), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(String data) {

            }
        });


//        try {
//            long timestamp = System.currentTimeMillis();
//            String time = formatter.format(new Date());
//            String fileName = "crash-" + time + "-" + timestamp + ".log";
//            if (Environment.getExternalStorageState().equals(
//                    Environment.MEDIA_MOUNTED)) {
//                String path = Environment.getExternalStorageDirectory()
//                        + "/crash/app异常退出Log/";
//                File dir = new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                FileOutputStream fos = new FileOutputStream(path + fileName);
//                fos.write(sb.toString().getBytes());
//                fos.close();
//            }
//            return fileName;
//        } catch (Exception e) {
//            L.e(TAG, "an error occured while writing file..." + e);
//        }
        return null;
    }
}
