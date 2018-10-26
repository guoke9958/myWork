package com.cn.xa.qyw.ui.slide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.dialog.CameraDialog;
import com.cn.xa.qyw.entiy.CameraOperate;
import com.cn.xa.qyw.entiy.SimpleBean;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.ui.userinfo.AddUserInfoActivity;
import com.cn.xa.qyw.ui.userinfo.ModifyUserIdActivity;
import com.cn.xa.qyw.ui.userinfo.ModifyUserPwdActivity;
import com.cn.xa.qyw.ui.userinfo.UserInfoUpdateActivity;
import com.cn.xa.qyw.utils.IFileUtils;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.rong.imageloader.utils.L;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/10/31.
 */
public class AccountCenterActivity extends DoctorBaseActivity {

    private TextView mTvUserName;
    private RelativeLayout mRlTopIcon, mRlAccount, mRlNickName, mRlModifyPwd;
    private SimpleDraweeView mIvTopIcon;
    private File mHeadCacheFile;
    private String mHeadCachePath;
    private TextView mTvNickName;
    private RelativeLayout mUserPush;
    private RelativeLayout mUserInvite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("会员中心");
        initView();
        initListener();
        initData();

        if (DoctorMainActivity.mUserInfo == null) {
            queryUserInfo();
        } else {
            initTipView();
        }

    }

    private void initTipView() {
        if (DoctorMainActivity.mUserInfo.getIsPass() == 0 || DoctorMainActivity.mUserInfo.getHospitalPass() == 0) {

            if (DoctorMainActivity.mUserInfo.getType() != 2 && DoctorMainActivity.mUserInfo.getType() != 3
                    && DoctorMainActivity.mUserInfo.getType() != 4 && DoctorMainActivity.mUserInfo.getType() != 5
                    && DoctorMainActivity.mUserInfo.getType() != 6 && DoctorMainActivity.mUserInfo.getType() != 7) {
                mTvNickName.setVisibility(View.VISIBLE);
            }
        } else {
            mTvNickName.setVisibility(View.GONE);
        }
    }

    public void queryUserInfo() {
        if (DoctorApplication.mUser != null) {
            String url = HttpAddress.GET_USER_INFO + "?data=" + DoctorApplication.mUser.getUserId();
            HttpUtils.postDataFromServer(url, new NetworkResponseHandler() {
                @Override
                public void onFail(String messsage) {

                }

                @Override
                public void onSuccess(String data) {

                    if (!StringUtils.isEmpty(data)) {
                        DoctorMainActivity.mUserInfo = JSONObject.parseObject(data, UserInfo.class);

                        io.rong.imlib.model.UserInfo userInfo = new io.rong.imlib.model.UserInfo(DoctorMainActivity.mUserInfo.getUserId(), StringUtils.getUserName(DoctorMainActivity.mUserInfo), Uri.parse(HttpAddress.PHOTO_URL + DoctorMainActivity.mUserInfo.getPhoto()));
                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                        RongIM.getInstance().setMessageAttachedUserInfo(true);

                        if (!StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getPhoto())) {
                            mIvTopIcon.setImageURI(HttpAddress.PHOTO_URL + DoctorMainActivity.mUserInfo.getPhoto());
                        }

                        initTipView();

                    } else {

                        new MaterialDialog.Builder(AccountCenterActivity.this)
                                .title("温馨提示")
                                .content("您的资料未完善，为更您带去更好的服务，请先完善您的个人信息！")
                                .cancelable(false)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        startActivity(getNewIntent(AddUserInfoActivity.class));
                                    }
                                })
                                .positiveText("去完善")
                                .show();

                    }

                }
            });
        }

    }

    private void initData() {

        if (DoctorApplication.mUser != null) {
            mTvUserName.setText(DoctorApplication.mUser.getUserName());
            if (DoctorMainActivity.mUserInfo != null && !StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getPhoto())) {
                mIvTopIcon.setImageURI(HttpAddress.PHOTO_URL + DoctorMainActivity.mUserInfo.getPhoto());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DoctorApplication.mUser != null) {
            mTvUserName.setText(DoctorApplication.mUser.getUserName());

            if (DoctorMainActivity.mUserInfo != null && !StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getPhoto())) {
                mIvTopIcon.setImageURI(HttpAddress.PHOTO_URL + DoctorMainActivity.mUserInfo.getPhoto());
            }

            queryUserInfo();

        }
    }

    private void initView() {
        mTvUserName = (TextView) findViewById(R.id.tv_mine_setting_username);
        mTvNickName = (TextView) findViewById(R.id.tv_mine_setting_nickname);
        mRlTopIcon = (RelativeLayout) findViewById(R.id.rl_top_icon);
        mRlAccount = (RelativeLayout) findViewById(R.id.rl_account);
        mRlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
        mRlModifyPwd = (RelativeLayout) findViewById(R.id.rl_modify_pwd);
        mIvTopIcon = (SimpleDraweeView) findViewById(R.id.imageView);
        mUserPush = (RelativeLayout)findViewById(R.id.rl_user_push);
        mUserInvite = (RelativeLayout)findViewById(R.id.rl_user_invite);
    }

    private void initListener() {
        mRlTopIcon.setOnClickListener(this);
        mRlAccount.setOnClickListener(this);
        mRlNickName.setOnClickListener(this);
        mRlModifyPwd.setOnClickListener(this);
        mUserPush.setOnClickListener(this);
        mUserInvite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.rl_top_icon) {
            doShowSetAvatarMenu();
        } else if (id == R.id.rl_modify_pwd) {
            startActivity(getNewIntent(ModifyUserPwdActivity.class));
        } else if (id == R.id.rl_nickname) {  //基本资料
            Intent intent = getNewIntent(UserInfoUpdateActivity.class);
            intent.putExtra("type", "modif");
            startActivity(intent);
        } else if (id == R.id.rl_account) {
            Intent intent = getNewIntent(ModifyUserIdActivity.class);
            DoctorApplication.mUser.setRequestType("modif");
            intent.putExtra("user", DoctorApplication.mUser);
            startActivityForResult(intent, 101, null);
        }else if(id == R.id.rl_user_push){
            startActivity(getNewIntent(UserPushActivity.class));
        }else if(id == R.id.rl_user_invite){
            showEditUserInvitePhone();
        }
    }

    private void showEditUserInvitePhone() {
        final EditText view = (EditText)getLayoutInflater().inflate(R.layout.custom_view, null);
        view.setText(DoctorApplication.mUser.getInvitePhone());
        view.setHint("请输入邀请人手机号");
        view.setInputType(InputType.TYPE_CLASS_PHONE);


        new MaterialDialog.Builder(this)
                .title("邀请人手机号设置")
                .customView(view, false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String content = view.getText().toString().trim();
                        updateContent(content);
                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .show();

    }

    private void updateContent(final String content) {

        if(!StringUtils.isTelActive(content)){
            showToast("手机号格式错误");
            return;
        }

        User user = new User();
        user.setUserId(DoctorApplication.mUser.getUserId());
        user.setInvitePhone(content);
        showDialog();
        HttpUtils.postDataFromServer(HttpAddress.UPDATE_USER_INVITE_PHONE, JSONObject.toJSONString(user), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                showToast("修改邀请人手机号失败");
            }

            @Override
            public void onSuccess(String data) {
                showToast("修改邀请人手机号成功");
                dismissDialog();
                DoctorApplication.mUser.setInvitePhone(content);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CameraOperate.REQ_GALLERY || requestCode == CameraOperate.REQ_CUT && data != null) {
                doUpdatePhoto(requestCode, data);
            } else if (requestCode == CameraOperate.REQ_TAKE_PHOTO) {
                doUpdatePhoto(requestCode, data);
            }
        }
    }

    /**
     * 我的头像设置菜单
     */
    private void doShowSetAvatarMenu() {
        CameraDialog dialog = new CameraDialog(this);
        dialog.show();
        // 拍照
        dialog.setCameraListener(new CameraDialog.CameraListener() {

            @Override
            public void onCamera() {
                // 调用系统的拍照功能,并指定调用相机拍照后照片的储存路径
                if (isSDCardActive()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File file = new File(IFileUtils.getAvatarFilePathCut());

                    if (file.exists()) {
                        file.delete();
                    }

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IFileUtils.getAvatarFilePath())));

                    startActivityForResult(intent, CameraOperate.REQ_TAKE_PHOTO);

                    // 提示SD卡不可用
                } else {
                    showToast("SD卡不可用");
                }
            }
        });

        // 相册
        dialog.setPhotoListener(new CameraDialog.PhotoListener() {

            @Override
            public void onPhoto() {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CameraOperate.REQ_GALLERY);
            }
        });
    }


    /**
     * 更新头像
     */
    private void doUpdatePhoto(int reqCode, Intent data) {
        if (reqCode == CameraOperate.REQ_TAKE_PHOTO) {
            doStartPhotoZoom(Uri.parse("file://" + IFileUtils.getAvatarFilePath()), 150);

        } else if (reqCode == CameraOperate.REQ_GALLERY) {
            if (data != null) {
                doStartPhotoZoom(data.getData(), 150);
            }

        } else if (reqCode == CameraOperate.REQ_CUT) {
            if (data != null) {
                doStorePicToLocal(data);
            }
        }
    }

    /**
     * 存储头像到本地
     */
    private void doStorePicToLocal(Intent data) {
        //
        // TODO 上传头像

//        upLoadAvatar(IFileUtils.getAvatarFilePathCut());

        new Thread(new Runnable() {
            @Override
            public void run() {
                httpUpload(IFileUtils.getAvatarFilePathCut());
            }
        }).start();


    }

    private void httpUpload(String fileName) {
        String boundary = "----WebKitFormBoundaryP0Rfzlf32iRoMhmb";
        //实际用到的时候会多两个杠杠          ------WebKitFormBoundaryP0Rfzlf32iRoMhmb
        String prefix = "--";
        String end = "\r\n";
        try {
            URL httpUrl = new URL(HttpAddress.UPDATE_PHOTO_FILE);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //通过multipart这种编码方式上传文件
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            //拼装要上传的文件
            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition: form-data;"
                    + "name=\"file\";filename=\"" + "Sky.jpg" + "\"" + end);
            out.writeBytes(end);
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            byte[] b = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.writeBytes(end);
            out.writeBytes(prefix + boundary + prefix + end);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            if (out != null) {
                out.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            Lg.e(sb.toString());
            showSuccess(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            dismissDialog();
            showToast("更换图像失败");
        } catch (IOException e) {
            e.printStackTrace();
            dismissDialog();
            showToast("更换图像失败");
        }
    }

    private void showSuccess(String result) {

        final SimpleBean bean = JSONObject.parseObject(result, SimpleBean.class);
        if (bean.getResult() == 0) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updatePhoto(bean.getData());
                }
            });
        } else {
            dismissDialog();
            showToast("更换图像失败");
        }


    }

    private void updatePhoto(final String data) {
        UserInfo info = new UserInfo();
        info.setUserId(DoctorApplication.mUser.getUserId());
        info.setPhoto(data);
        HttpUtils.postDataFromServer(HttpAddress.UPLOAD_USER_PHOTO, JSONObject.toJSONString(info), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                showToast("更换图像失败");
            }

            @Override
            public void onSuccess(String result) {
                dismissDialog();
                showToast("更换图像成功");

                DoctorMainActivity.mUserInfo.setPhoto(data);
                if (!StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getPhoto())) {
                    mIvTopIcon.setImageURI(HttpAddress.PHOTO_URL + DoctorMainActivity.mUserInfo.getPhoto());
                }
            }
        });
    }

    //文件上传
    public void upLoadAvatar(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                RequestParams params = new RequestParams();

                params.put("image", file, "application/octet-stream");

                params.put("file", file);
                params.put("data", DoctorApplication.mUser.getUserId());
                HttpUtils.postDataFromServer(HttpAddress.UPDATE_PHOTO_FILE, params, new NetworkResponseHandler() {
                    @Override
                    public void onFail(String messsage) {
                        dismissDialog();
                        showToast("更换图像失败");
                    }

                    @Override
                    public void onSuccess(String data) {
                        dismissDialog();
                        showToast("更换图像成功");

                    }
                });


            } else {
                ToastUtils.showShortToast(this, "文件不存在");
            }
        } catch (Exception e) {
            L.e(e);
        }
    }


    /**
     * 执行裁剪
     */
    private void doStartPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);

        mHeadCacheFile = new File(IFileUtils.getAvatarFilePathCut());
        if (!mHeadCacheFile.exists()) {
            try {
                mHeadCacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mHeadCachePath = mHeadCacheFile.getAbsolutePath();

        Uri uriPath = Uri.parse("file://" + mHeadCacheFile.getAbsolutePath());
        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //注意：此处应设置return-data为false，如果设置为true，是直接返回bitmap格式的数据，耗费内存。设置为false，然后，设置裁剪完之后保存的路径，即：intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
        //intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);

        startActivityForResult(intent, CameraOperate.REQ_CUT);
    }

    /**
     * 检测SD卡是否可用
     */
    public static boolean isSDCardActive() {
        boolean isActive = false;

        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            isActive = true;
        }

        return isActive;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_mine_account_setting;
    }
}
