package com.cn.xa.qyw.ui.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.HospitalDetailInfo;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.view.AddUserInfoItemView;
import com.cn.xa.qyw.view.UserInfoItemView;

import java.util.ArrayList;
import java.util.List;

/**
 *  基本资料
 * Created by Administrator on 2016/10/31.
 */
public class UserInfoUpdateActivity extends DoctorBaseActivity {

    private LinearLayout mParent;
    private UserInfo mUserInfo;
    private UserInfoItemView mItemViewCity;
    private UserInfoItemView mItemViewArea;
    private List<UserInfoItemView> mList = new ArrayList<>();
    private UserInfo info;
    private String mType;
    private UserInfoItemView mHospitalItem;
    private UserInfoItemView mHospitalGrade;
    private UserInfoItemView mDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getStringExtra("type");
        mToolbarTitle.setText("基本信息维护");
        mToolbarRight.setVisibility(View.VISIBLE);
        mToolbarRight.setText("编辑");
        initView();
        mToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRight();
            }
        });
    }

    private void initView() {
        mParent = (LinearLayout) findViewById(R.id.user_info_parent);
        showDialog();
        String url = HttpAddress.GET_USER_INFO + "?data=" + DoctorApplication.mUser.getUserId();
        HttpUtils.postDataFromServer(url, new NetworkResponseHandler() {
            @Override
            public void onFail(String messsage) {
                dismissDialog();
                showToast("获取信息失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                mUserInfo = JSONObject.parseObject(data, UserInfo.class);

                if(StringUtils.isEmpty(mUserInfo.getPhone())){
                    try {
                        int l = Integer.parseInt(DoctorApplication.mUser.getUserName());
                        mUserInfo.setPhone(DoctorApplication.mUser.getUserName());
                    }catch (Exception e){

                    }
                }

                addParent();
            }
        });

    }

    private void addParent() {
        int type = DoctorApplication.mUser.getType();
        if (type == 1) {
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "name"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "sex"));
            mHospitalItem = new UserInfoItemView(this, mUserInfo, mParent, "hospital");
            mList.add(mHospitalItem);
            mHospitalGrade = new UserInfoItemView(this, mUserInfo,mParent, "hospitalGrade");
            mList.add(mHospitalGrade);
            mDepartment = new UserInfoItemView(this, mUserInfo, mParent, "department");
            mList.add(mDepartment);
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "professional"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "duty"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "phone"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "normalAdc"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "phoneAdv"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "bigAdc"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "familyAdc"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "healthAdc"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "goodAt"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "intro"));
        }else if (type == 2) {
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "name"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "true_name"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "sex"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "phone"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "birthday"));
            UserInfoItemView itemViewProvince = new UserInfoItemView(this, mUserInfo, mParent, "province");
            mItemViewCity = new UserInfoItemView(this, mUserInfo, mParent, "city", itemViewProvince);
            mItemViewArea = new UserInfoItemView(this, mUserInfo, mParent, "area", mItemViewCity);
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "addressDetail"));
            mList.add(itemViewProvince);
            mList.add(mItemViewCity);
            mList.add(mItemViewArea);
        }else if (type == 3||type == 4
            ||type == 5||type == 6) {
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "name"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "true_name"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "phone"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "category"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "duty"));
            UserInfoItemView itemViewProvince = new UserInfoItemView(this, mUserInfo, mParent, "province");
            mItemViewCity = new UserInfoItemView(this, mUserInfo, mParent, "city", itemViewProvince);
            mItemViewArea = new UserInfoItemView(this, mUserInfo, mParent, "area", mItemViewCity);
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "addressDetail"));
            mList.add(itemViewProvince);
            mList.add(mItemViewCity);
            mList.add(mItemViewArea);
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "goodAt"));
            mList.add(new UserInfoItemView(this, mUserInfo, mParent, "intro"));
        }

    }


    public void initArea() {
        mItemViewArea.setContent();
    }

    public void selectCity() {
        mItemViewCity.show();
    }

    public void selectArea() {
        mItemViewArea.show();
    }

    public void initCityAndArea() {
        mItemViewCity.setContent();
        mItemViewArea.setContent();
    }

    private void showRight() {
        if (mList.get(0).isVisiable()) {
            Lg.e("提交信息");
            sendMsgServer();

        } else {
            mToolbarRight.setText("提交");
            for (UserInfoItemView itemView : mList) {
                itemView.initViewVisiable(true);
            }
        }


    }

    private void sendMsgServer() {
        info = new UserInfo();
        info.setUserId(DoctorApplication.mUser.getUserId());
        info.setType(DoctorApplication.mUser.getType());
        info.setPhoto(mUserInfo.getPhoto());
        for (UserInfoItemView item : mList) {
            info = item.getUserInfo(info);
            if (info == null) {
                Lg.e("信息不完整");
                break;
            }
        }

        if (info != null) {
            showDialog();
            HttpUtils.postDataFromServer(HttpAddress.UPDATE_USER_INFO, JSONObject.toJSONString(info), new NetworkResponseHandler() {
                @Override
                public void onFail(String message) {
                    dismissDialog();
                    showToast("更新失败");
                }

                @Override
                public void onSuccess(String data) {
                    dismissDialog();
                    mToolbarRight.setText("编辑");
                    for (UserInfoItemView itemView : mList) {
                        itemView.initViewVisiable(false);
                        itemView.setUserInfo(info);
                    }
                    DoctorMainActivity.mUserInfo.setNickName(info.getNickName());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mList != null && mList.get(0).isVisiable()) {
            mToolbarRight.setText("编辑");
            for (UserInfoItemView itemView : mList) {
                itemView.initViewVisiable(false);
            }
            return;
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 16){
                HospitalDetailInfo info = (HospitalDetailInfo) data.getSerializableExtra("detailInfo");
                Lg.e(JSONObject.toJSONString(info));

                if(mHospitalItem != null){
                    mHospitalItem.setHospitalInfo(info.getId(),info.getHospital_name());
                }

                if (mHospitalGrade != null) {
                    mHospitalGrade.setHospitalGrade(info.getIs_pass(),info.getHospital_grade(),info.getHospital_type());
                }

                if (mDepartment != null) {
                    mDepartment.setmHospitalType(info.getHospital_type());
                }

            }
        }
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_user_info;
    }
}
