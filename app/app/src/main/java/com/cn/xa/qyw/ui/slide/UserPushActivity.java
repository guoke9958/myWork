package com.cn.xa.qyw.ui.slide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.dialog.CameraDialog;
import com.cn.xa.qyw.dialog.SelectCityDialog;
import com.cn.xa.qyw.entiy.CameraOperate;
import com.cn.xa.qyw.entiy.Push;
import com.cn.xa.qyw.entiy.SimpleBean;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.utils.IFileUtils;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 409160 on 2017/2/13.
 */
public class UserPushActivity extends DoctorBaseActivity {

    private EditText mEditContent;
    private Button mBtnPushType;
    private Button mBtnPushCity;
    private Button mBtnPushProvince;
    int mPosition = 0;
    private TextView mServiceGuize;
    private CheckBox mCheckBox;
    private boolean mIsChecked = true;
    private TextView mSendCount;
    private SimpleDraweeView mAddImage;
    private File mHeadCacheFile;
    private String mHeadCachePath;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("消息推送");
        mToolbarRight.setVisibility(View.VISIBLE);
        mToolbarRight.setText("确定");
        initView();
        initListener();

        getData();
    }

    private void getData() {
        HttpUtils.postDataFromServer(HttpAddress.GET_USER_VOUCHER, DoctorApplication.mUser.getUserId(), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(String data) {
                int total = Integer.parseInt(data);
                int count = total / 2;

                String str = "您还可再发送 <font color='red' size='36'>" + count + "</font> 次消息（一次2元）";
                mSendCount.setText(Html.fromHtml(str));
            }
        });
    }

    private void initView() {
        mBtnPushProvince = (Button) findViewById(R.id.push_province);
        mBtnPushCity = (Button) findViewById(R.id.push_city);
        mBtnPushType = (Button) findViewById(R.id.push_to_user_type);
        mEditContent = (EditText) findViewById(R.id.push_content);
        mServiceGuize = (TextView) findViewById(R.id.server_guize);
        mCheckBox = (CheckBox) findViewById(R.id.sure_push);
        mServiceGuize.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mServiceGuize.getPaint().setAntiAlias(true);//抗锯齿
        mSendCount = (TextView) findViewById(R.id.send_count);
        mAddImage = (SimpleDraweeView)findViewById(R.id.add_push_img);
    }

    private void initListener() {
        mToolbarRight.setOnClickListener(this);
        mBtnPushProvince.setOnClickListener(this);
        mBtnPushCity.setOnClickListener(this);
        mBtnPushType.setOnClickListener(this);
        mAddImage.setOnClickListener(this);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChecked = isChecked;
            }
        });

        mServiceGuize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getNewIntent(PushServiceGuiZeActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mToolbarRight.getId()) {
            sendToServerPush();
        } else if (id == mBtnPushProvince.getId()) {
            showSelectProvince();
        } else if (id == mBtnPushCity.getId()) {
            String province = mBtnPushProvince.getText().toString().trim();
            if ("全国".equals(province)) {
                showToast("请先选择省份");
            } else {
                selectCity(province);
            }
        } else if (id == mBtnPushType.getId()) {
            showUserType();
        }else if(v == mAddImage){
            doShowSetAvatarMenu();
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
     * 检测SD卡是否可用
     */
    public static boolean isSDCardActive() {
        boolean isActive = false;

        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            isActive = true;
        }

        return isActive;
    }


    private void sendToServerPush() {

        if (mIsChecked) {
            String content = mEditContent.getText().toString().trim();
            if (!StringUtils.isEmpty(content)) {
                if (content.length() < 5) {
                    showToast("推送内容长度必须大于5个字");
                }else if(content.length() > 200){
                    showToast("推送内容过长");
                } else {
                  sendServer(content,mPath);
                }
            } else {
                showToast("推送内容不能为空");
            }
        } else {
            showToast("请勾选服务条款");
        }

    }

    private void sendServer(String content,String path) {
        String province = mBtnPushProvince.getText().toString().trim();
        String city = mBtnPushCity.getText().toString().trim();
        Push push = new Push();
        push.setProvince(province);
        push.setCity(city);
        push.setContent(content);
        push.setImgPath(path);
        push.setUserId(DoctorApplication.mUser.getUserId());
        push.setUserType(mPosition);
        HttpUtils.postDataFromServer(HttpAddress.PUSH_CONTENT_ALL, JSONObject.toJSONString(push), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                showToast("发送失败，请重试");
            }

            @Override
            public void onSuccess(String data) {
                showToast("发送成功");
                finish();
            }
        });
    }

    private void showSelectProvince() {
        new SelectCityDialog(this, 0, "", false, false, new SelectCityDialog.OnSelectListener() {
            @Override
            public void onSelect(String city) {
                String content = mBtnPushProvince.getText().toString().trim();
                if (!city.equals(content)) {
                    mBtnPushProvince.setText(city);
                    mBtnPushCity.setText("所有市区");
                    if (!"全国".equals(city)) {
                        selectCity(city);
                    }
                }
            }
        }, 1);
    }

    private void selectCity(String province) {
        new SelectCityDialog(this, 1, province, false, false, new SelectCityDialog.OnSelectListener() {
            @Override
            public void onSelect(String city) {
                mBtnPushCity.setText(city);
            }
        }, 1);
    }


    private void showUserType() {
        new MaterialDialog.Builder(this)
                .title("选择会员类型")
                .items(R.array.push_user_type)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        mPosition = which;
                        mBtnPushType.setText(text);
                    }
                })
                .positiveText("确定")
                .show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CameraOperate.REQ_GALLERY || requestCode == CameraOperate.REQ_CUT && data != null) {
                doUpdatePhoto(requestCode, data);
            } else if (requestCode == CameraOperate.REQ_TAKE_PHOTO) {
                doUpdatePhoto(requestCode, data);
            }else if (requestCode == CameraOperate.REQ_CUT) {
                if (data != null) {
                    doStorePicToLocal(data);
                }
            }
        }
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
     * 存储头像到本地
     */
    private void doStorePicToLocal(Intent data) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                httpUpload(IFileUtils.getAvatarFilePathCut());
            }
        }).start();

        //
        // TODO 上传头像

//        upLoadAvatar(IFileUtils.getAvatarFilePathCut());

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
            showToast("图像上传失败");
        } catch (IOException e) {
            e.printStackTrace();
            dismissDialog();
            showToast("图像上传失败");
        }
    }

    private void showSuccess(String result) {

        final SimpleBean bean = JSONObject.parseObject(result, SimpleBean.class);
        if (bean.getResult() == 0) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPath = HttpAddress.PHOTO_URL + bean.getData();
                    mAddImage.setImageURI(mPath);
                }
            });
        } else {
            dismissDialog();
            showToast("图像上传失败");
        }


    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_user_push;
    }
}
