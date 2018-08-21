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
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.view.AddUserInfoItemView;
import com.cn.xa.qyw.view.UserInfoItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 409160 on 2016/12/7.
 */
public class AddUserInfoActivity extends DoctorBaseActivity {

    private LinearLayout mParent;
    private AddUserInfoItemView mItemViewCity;
    private AddUserInfoItemView mItemViewArea;

    private List<AddUserInfoItemView> mList = new ArrayList<>();
    private UserInfo info;
    private AddUserInfoItemView mHospitalItem;
    private AddUserInfoItemView mHospitalGrade;
    private AddUserInfoItemView mDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("添加基本信息");
        mToolbarRight.setVisibility(View.VISIBLE);
        mToolbarRight.setText("完成");
        initView();
    }

    private void initView() {
        mParent = (LinearLayout) findViewById(R.id.user_info_parent);
        initData();
        mToolbarRight.setOnClickListener(this);
    }

    private void initData() {

        if (DoctorApplication.mUser != null) {
            if (DoctorApplication.mUser.getType() == 1) {
                mList.add(new AddUserInfoItemView(this, mParent, "true_name", true));
                mList.add(new AddUserInfoItemView(this, mParent, "sex", true));
                mHospitalItem = new AddUserInfoItemView(this, mParent, "hospital", true);
                mList.add(mHospitalItem);
                mHospitalGrade = new AddUserInfoItemView(this, mParent, "hospitalGrade", true);
                mList.add(mHospitalGrade);
                mDepartment = new AddUserInfoItemView(this, mParent, "department", true);
                mList.add(mDepartment);
                mList.add(new AddUserInfoItemView(this, mParent, "professional", true));
                mList.add(new AddUserInfoItemView(this, mParent, "duty", false));
                mList.add(new AddUserInfoItemView(this, mParent, "phone", true));
                mList.add(new AddUserInfoItemView(this, mParent, "normalAdc", false));
                mList.add(new AddUserInfoItemView(this, mParent, "phoneAdv", false));
                mList.add(new AddUserInfoItemView(this, mParent, "bigAdc", false));
                mList.add(new AddUserInfoItemView(this, mParent, "familyAdc", false));
                mList.add(new AddUserInfoItemView(this, mParent, "healthAdc", false));
                mList.add(new AddUserInfoItemView(this, mParent, "goodAt", false));
                mList.add(new AddUserInfoItemView(this, mParent, "intro", false));

            } else if (DoctorApplication.mUser.getType() == 2) {
                mList.add(new AddUserInfoItemView(this, mParent, "name", true));
                mList.add(new AddUserInfoItemView(this, mParent, "true_name", true));
                mList.add(new AddUserInfoItemView(this, mParent, "sex", true));
                mList.add(new AddUserInfoItemView(this, mParent, "phone", true));
                mList.add(new AddUserInfoItemView(this, mParent, "birthday", true));
                AddUserInfoItemView itemViewProvince = new AddUserInfoItemView(this, mParent, "province", true);
                mList.add(itemViewProvince);
                mItemViewCity = new AddUserInfoItemView(this, mParent, "city", true, itemViewProvince);
                mItemViewArea = new AddUserInfoItemView(this, mParent, "area", true, mItemViewCity);
                AddUserInfoItemView itemViewAddressDetail = new AddUserInfoItemView(this, mParent, "addressDetail", true);
                mList.add(mItemViewCity);
                mList.add(mItemViewArea);
                mList.add(itemViewAddressDetail);

            }else if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                    ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                mList.add(new AddUserInfoItemView(this, mParent, "name", true));
                mList.add(new AddUserInfoItemView(this, mParent, "true_name", true));
                mList.add(new AddUserInfoItemView(this, mParent, "phone", true));
                mList.add(new AddUserInfoItemView(this, mParent, "category", true));
                mList.add(new AddUserInfoItemView(this, mParent, "duty", true));
                AddUserInfoItemView itemViewProvince = new AddUserInfoItemView(this, mParent, "province", true);
                mList.add(itemViewProvince);
                mItemViewCity = new AddUserInfoItemView(this, mParent, "city", true, itemViewProvince);
                mItemViewArea = new AddUserInfoItemView(this, mParent, "area", true, mItemViewCity);
                AddUserInfoItemView itemViewAddressDetail = new AddUserInfoItemView(this, mParent, "addressDetail", true);
                mList.add(mItemViewCity);
                mList.add(mItemViewArea);
                mList.add(itemViewAddressDetail);
                mList.add(new AddUserInfoItemView(this, mParent, "goodAt", false));
                mList.add(new AddUserInfoItemView(this, mParent, "intro", false));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 16) {
                HospitalDetailInfo info = (HospitalDetailInfo) data.getSerializableExtra("detailInfo");
                Lg.e(JSONObject.toJSONString(info));

                if (mHospitalItem != null) {
                    mHospitalItem.setHospitalInfo(info.getId(), info.getHospital_name());
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
    public void onClick(View v) {
        info = new UserInfo();
        info.setUserId(DoctorApplication.mUser.getUserId());
        info.setType(DoctorApplication.mUser.getType());
        for (AddUserInfoItemView item : mList) {
            info = item.setUserInfo(info);
            if (info == null) {
                Lg.e("信息不完整");
                break;
            }
        }

        if (info != null) {
            Lg.e("提交信息");
            showDialog();

            if (info.getType() == 1) {
                info.setNickName(info.getTrueName());
                info.setIsPass(0);
            } else {
                info.setIsPass(1);
            }

            HttpUtils.postDataFromServer(HttpAddress.ADDUSER_INFO, JSONObject.toJSONString(info), new NetworkResponseHandler() {
                @Override
                public void onFail(String message) {
                    dismissDialog();
                    showToast("添加用户信息失败");
                }

                @Override
                public void onSuccess(String data) {
                    dismissDialog();
                    showToast("添加用户信息成功");
                    Intent intent = getNewIntent(UserPhotoUpdateActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_user_info;
    }

    public void initCityAndArea() {
        mItemViewCity.setContent();
        mItemViewArea.setContent();
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
}
