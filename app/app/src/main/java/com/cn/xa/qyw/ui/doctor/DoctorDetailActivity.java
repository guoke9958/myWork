package com.cn.xa.qyw.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.dialog.DoctorBaseDialog;
import com.cn.xa.qyw.entiy.DoctorDetailInfo;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/8/13.
 */
public class DoctorDetailActivity extends DoctorBaseActivity {

    private SimpleDoctor mDoctor;
    private View mBtnZixun;
    private SimpleDraweeView mDoctorPhoto;
    private TextView mDoctorState;
    private TextView mDoctorName;
    private TextView mDepartName;
    private TextView mDoctorZC;
    private TextView mHospitalName;
    private TextView mGoodAt;
    private View mParent;
    private TextView jianjie;
    private TextView goodAt;
    private UserInfo mInfo;
    private TextView mUserPhone;
    private View mYuYue;
    private String mGrade;
    private String mGradeId;
    private View mHospitalParent;
    private String mDoctorId;
    private View mBtnNorZixun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDoctor = (SimpleDoctor) getIntent().getSerializableExtra("doctor");
        mDoctorId = mDoctor.getDoctorId();
        mGrade = getIntent().getStringExtra("grade");
        mGradeId = getIntent().getStringExtra("grade_id");
        mToolbarTitle.setText(mDoctor.getNickName() + "主页");
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mParent = findViewById(R.id.parent);
        mParent.setVisibility(View.GONE);
        mBtnZixun = findViewById(R.id.btn_zixun);
        mBtnNorZixun = findViewById(R.id.btn_nomarl_zixun);
        mDoctorPhoto = (SimpleDraweeView) findViewById(R.id.doctor_photo);
        mDoctorState = (TextView) findViewById(R.id.doctor_state);
        mDoctorName = (TextView) findViewById(R.id.doctor_name);
        mDepartName = (TextView) findViewById(R.id.doctor_department);
        mDoctorZC = (TextView) findViewById(R.id.doctor_zhicheng);
        mHospitalParent = findViewById(R.id.hospital_parent);
        mHospitalName = (TextView) findViewById(R.id.doctor_hospital_name);
        mGoodAt = (TextView) findViewById(R.id.good_at);
        jianjie = (TextView) findViewById(R.id.jianjie);
        goodAt = (TextView) findViewById(R.id.goodAt);
        mUserPhone = (TextView) findViewById(R.id.doctor_user_phone);
        mYuYue = findViewById(R.id.yuyue);

    }


    private void initListener() {
        mBtnZixun.setOnClickListener(this);
        jianjie.setOnClickListener(this);
        goodAt.setOnClickListener(this);
        mYuYue.setOnClickListener(this);
        mBtnNorZixun.setOnClickListener(this);
    }

    private void initData() {
        showDialog();
        HttpUtils.postDataFromServer(HttpAddress.GET_USER_INFO, mDoctor.getDoctorId(), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                mParent.setVisibility(View.VISIBLE);
                ToastUtils.showShortSnackbar(mToolbar, message);
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                UserInfo info = JSONObject.parseObject(data, UserInfo.class);
                mDoctor.setDoctorId(info.getUserId());
                initViewData(info);
                mParent.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initViewData(UserInfo info) {
        this.mInfo = info;

        if (!StringUtils.isEmpty(info.getPhoto())) {
            if (info.getPhoto().contains("http://")) {
                mDoctorPhoto.setImageURI(info.getPhoto());
            } else {
                mDoctorPhoto.setImageURI(HttpAddress.PHOTO_URL + info.getPhoto());
            }
        }

        if ("1".equals(info.getState())) {
            mDoctorState.setText("状态：在线");
        } else {
            mDoctorState.setText("状态：离线");
        }

        if (info.getType() == 3 || info.getType() == 4
                || info.getType() == 5 || info.getType() == 6) {
            mDoctorName.setText("姓名：" + info.getNickName());
            goodAt.setVisibility(View.GONE);

            mHospitalParent.setVisibility(View.GONE);
            findViewById(R.id.artile_parent).setVisibility(View.GONE);
            findViewById(R.id.guahao_parent).setVisibility(View.GONE);
            findViewById(R.id.yinanfenxi_parent).setVisibility(View.GONE);
            findViewById(R.id.jiating_parent).setVisibility(View.GONE);

        } else {
            mDoctorName.setText("姓名：" + info.getTrueName());
        }


        if (info.getType() == 3 || info.getType() == 4
                || info.getType() == 5 || info.getType() == 6) {
            mDepartName.setText("类别：" + info.getDepartmentName());
        } else {
            mDepartName.setText("科室：" + info.getDepartmentName());
        }


        if (info.getType() == 3 || info.getType() == 4
                || info.getType() == 5 || info.getType() == 6) {
            if (StringUtils.isEmpty(info.getDuty())) {
                mDoctorZC.setText("专业：暂未填写");
            } else {
                mDoctorZC.setText("专业：" + info.getDuty());
            }
        } else {
            if (StringUtils.isEmpty(info.getProfessionalTitle())) {
                mDoctorZC.setText("职称：暂未填写");
            } else {
                mDoctorZC.setText("职称：" + info.getProfessionalTitle());
            }
        }


        if (info.getType() == 3 || info.getType() == 4
                || info.getType() == 5 || info.getType() == 6) {
            mHospitalName.setText("单位名称：" + info.getTrueName());
        } else {
            mHospitalName.setText("医院：" + info.getHospitalName());
        }

        if (StringUtils.isEmpty(info.getPhone())) {
            mUserPhone.setText("暂无手机号");
        } else {

            String phone = info.getPhone();
            mUserPhone.setText(StringUtils.subPhoneString(phone));
        }
        initSelected(true, info);
    }

    private void initSelected(boolean isTrue, UserInfo info) {
        if (isTrue) {
            jianjie.setBackgroundResource(R.color.main_toolbar_normal);
            jianjie.setTextColor(getResources().getColor(R.color.white));
            goodAt.setTextColor(getResources().getColor(R.color.black));
            goodAt.setBackgroundResource(R.color.white);

            if (info != null) {

                if (!StringUtils.isEmpty(info.getIntro())) {
                    mGoodAt.setText(Html.fromHtml(info.getIntro()));
                    mGoodAt.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    mGoodAt.setGravity(Gravity.CENTER);
                    mGoodAt.setText("暂无");
                }

            }

        } else {
            goodAt.setBackgroundResource(R.color.main_toolbar_normal);
            jianjie.setBackgroundResource(R.color.white);
            goodAt.setTextColor(getResources().getColor(R.color.white));
            jianjie.setTextColor(getResources().getColor(R.color.black));

            if (info != null) {
                if (!StringUtils.isEmpty(info.getGoodAt())) {
                    mGoodAt.setText(Html.fromHtml(info.getGoodAt()));
                    mGoodAt.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    mGoodAt.setGravity(Gravity.CENTER);
                    mGoodAt.setText("暂无");
                }
            }

        }
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_docotr_detail;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.btn_zixun|| v == mBtnNorZixun) {
            if (!DoctorBaseDialog.isShowLoginDialog(this)) {

                if (DoctorApplication.mUser.getUserId().equals(mDoctor.getDoctorId())) {
                    showCofiDialog();
                } else {
                    RongIM.getInstance().startPrivateChat(this, mDoctor.getDoctorId(), mDoctorId + ";" + mDoctor.getDoctorName());
                }

            }

        } else if (id == R.id.jianjie) {
            initSelected(true, mInfo);
        } else if (id == R.id.goodAt) {
            initSelected(false, mInfo);
        } else if (id == R.id.yuyue) {

            if (!DoctorBaseDialog.isShowLoginDialog(this)) {
                if (mInfo != null) {
                    if (!StringUtils.isEmpty(mInfo.getPhone())) {
                        Intent intent = getNewIntent(DoctorOrderActivity.class);
                        intent.putExtra("phone", mInfo.getPhone());
                        intent.putExtra("doctorid", mInfo.getUserId());
                        intent.putExtra("doctorName", mInfo.getTrueName());
                        intent.putExtra("grade", mGrade);
                        startActivity(intent);
                    } else {
                        showToast("该医生暂未开通预约服务");
                    }
                } else {
                    initData();
                }
            }
        }
    }

    private void showCofiDialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title("温馨提示")
                .content("您不能可自己进行聊天")
                .positiveText("取消")
                .show();
    }
}
