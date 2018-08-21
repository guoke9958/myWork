package com.cn.xa.qyw.update;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.App;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.utils.Lg;
import com.loopj.android.http.BinaryHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by cn on 15-6-5.
 */
public class WantUpdate {

    private Activity mContext;
    private String url;
    private PreferenceUtils preferenece;
    private MaterialDialog mDialog;
    private MaterialDialog.Builder mBuilder;
    private String mUrl;
    private boolean mIsForce;

    private WantUpdate(Activity context) {
        mContext = context;
        preferenece = PreferenceUtils.getInstance();
        try {

            HttpUtils.getDataFromServer(HttpAddress.GET_VERSIOIN_INFO, new NetworkResponseHandler() {
                @Override
                public void onFail(String error) {

                }

                @Override
                public void onSuccess(String data) {
                    App app = JSONObject.parseObject(data,
                            App.class);
                    isUpdate(app);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void isUpdate(App app) {
        final int versionCode = getVersionCode();

        int serverCode = app.getVersionCode();
        url = app.getDowndloadUrl();

        if (serverCode > versionCode) {
            showUpdateDialog(app);
            return;
        }
//
//		if (getUpdateContent()) {
//			showTipDialog(app);
//			setUpdateContent();
//			return;
//		}

    }

    public void setUpdateContent() {
        preferenece.setPrefBoolean("show_update_content"
                + getVersionCode(), false);
    }

    public boolean getUpdateContent() {
        return preferenece.getPrefBoolean("show_update_content"
                + getVersionCode(), true);
    }

    private void showTipDialog(App app) {
        String content = getUpdateString(app.getVersionName(),
                app.getUpdateContext());

        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .theme(Theme.LIGHT)
                .title("更新内容")
                .content(content)
                .positiveText("OK")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                    }
                })
                .show();

        dialog.setCanceledOnTouchOutside(true);

    }

    private void showUpdateDialog(final App app) {
        Integer isForce = app.getIsFlag();
        String content = getUpdateString(app.getVersionName(),
                app.getUpdateContext());
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);

        builder.theme(Theme.LIGHT);
        builder.title("发现新版本");
        builder.content(content);
        builder.positiveText("确认更新");
        mIsForce = (isForce == 0 ? false : true);
        if (isForce == 0) {
            builder.negativeText("取消");
        }

        builder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                downloadAppFile(app.getDowndloadUrl(), app.getIsFlag());
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
            }
        });

        MaterialDialog dialog = builder.show();

        dialog.setCanceledOnTouchOutside(false);

        if (isForce != 0) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return true;
                }
            });
        }
    }

    private String getUpdateString(String versionName, String content) {

        if (content.contains("_")) {
            String[] arr = content.split("_");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                sb.append((i + 1) + " : ");
                sb.append(arr[i]);
                sb.append("\n");
            }
            return "新版本号：" + versionName + "\n" + sb.toString();
        } else if (!TextUtils.isEmpty(content)) {
            return "新版本号：" + versionName + "\n" + "1 : " + content;
        } else {
            return "";
        }
    }

    private void sendDownloadCmd() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        mContext.startActivity(intent);
        mContext.finish();
    }

    public interface isContinueUpdate {
        public void noUpdate();
    }

    public static WantUpdate init(Activity context) {
        return new WantUpdate(context);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    private int getVersionCode() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            if (info != null) {
                return info.versionCode;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;

    }


    String[] allowedContentTypes = new String[]{"application/vnd.android.package-archive"};


    BinaryHttpResponseHandler hander = new BinaryHttpResponseHandler(allowedContentTypes) {

        @Override
        public void onSuccess(int state, Header[] arg1,
                              byte[] data) {
            Lg.e("下载完成-=length=" + data.length + "===state="
                    + state);

            saveApp(data);

            if (mDialog != null) {
                mDialog.dismiss();
            }

            installApk();

        }

        @Override
        public void onFailure(int arg0, Header[] arg1,
                              byte[] arg2, Throwable arg3) {
            Lg.e("下载失败");

            if (mDialog != null) {
                mDialog.dismiss();
            }


            new MaterialDialog.Builder(mContext)
                    .title("下载对话框")
                    .content("下载失败")
                    .cancelable(!mIsForce)
                    .positiveText("重新下载")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            downloadAppFile(mUrl, 1);
                        }
                    })
                    .show();
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
            Lg.e("开始进度==" + count);

            if (mDialog != null) {
                mDialog.setProgress(count);
            }

        }

    };

    protected void downloadAppFile(String url, int isForce) {
        mUrl = url;

        if (!url.contains("http:")) {
            mUrl = "http://www.qiuyiwang.com:8081" + url;
        }

        mDialog = showProgressDeterminateDialog(isForce);
        hander.setTag("file");
        try {

            HttpUtils.downloadFile(mContext, mUrl, hander);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void saveApp(byte[] data) {
        try {
            String fileName = "law.apk";
            String path = Environment.getExternalStorageDirectory()
                    + "/crash/app/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(path + fileName);
            if (!file.exists()) {
                file.delete();
            }


            final FileOutputStream output = new FileOutputStream(path
                    + fileName);
            output.write(data, 0, data.length);
            output.flush();
            output.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MaterialDialog showProgressDeterminateDialog(int isForce) {
        mBuilder = new MaterialDialog.Builder(mContext)
                .title("下载对话框")
                .content("正在下载...")
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 100, false)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                        Lg.e("取消升级======================");

                        HttpUtils.cancelAllRequest(mContext);
                    }
                });

        if (!mIsForce) {
            mBuilder.positiveText("取消升级");
        }

        return mBuilder.show();
    }

    protected void installApk() {
        // 核心是下面几句代码
        String path = Environment.getExternalStorageDirectory()
                + "/crash/app/law.apk";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivityForResult(intent, 0);// 如果用户取消安装的话,
        // 会返回结果,回调方法onActivityResult
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
