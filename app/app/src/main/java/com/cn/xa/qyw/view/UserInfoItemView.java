package com.cn.xa.qyw.view;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.dialog.SelectCityDialog;
import com.cn.xa.qyw.entiy.AddDepartments;
import com.cn.xa.qyw.entiy.HospitalDetailInfo;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.hospital.SelectHospitalActivity;
import com.cn.xa.qyw.ui.userinfo.UserInfoUpdateActivity;
import com.cn.xa.qyw.utils.DensityUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

/**
 * Created by 409160 on 2016/12/5.
 */
public class UserInfoItemView {

    private int mHospitalType;
    private String mType;
    private UserInfoUpdateActivity mActivity;
    private UserInfoItemView mItemView;
    private UserInfo mInfo;
    private TextView mTitle;
    private TextView mContent;
    private TextView mEdit;
    private int mIsPass;
    private int mCategoryId;

    public UserInfoItemView(UserInfoUpdateActivity activity, UserInfo info, LinearLayout parent, String type) {
        mType = type;
        mActivity = activity;
        mIsPass = info.getHospitalPass();
        mHospitalType = info.getHospitalType();
        LayoutInflater mInflater = activity.getLayoutInflater();
        mInfo = info;

        View view = mInflater.inflate(R.layout.item_user_info, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        initView(view);
        initData(type);
        initListener();

        if ("goodAt".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        } else if ("intro".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        }

        parent.addView(view, lp);

    }

    public UserInfoItemView(UserInfoUpdateActivity activity, UserInfo info, LinearLayout parent, String type, UserInfoItemView itemView) {
        mType = type;
        mActivity = activity;
        mItemView = itemView;
        mIsPass = info.getHospitalPass();
        mHospitalType = info.getHospitalType();
        LayoutInflater mInflater = activity.getLayoutInflater();
        mInfo = info;

        View view = mInflater.inflate(R.layout.item_user_info, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        initView(view);
        initData(type);
        initListener();

        if ("goodAt".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        } else if ("intro".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        }

        parent.addView(view, lp);

    }

    private void initListener() {
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = "";
                if ("name".equals(mType)) {
                    mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    if (DoctorApplication.mUser.getType() == 1) {
                        hint = "请输入姓名";
                    } else if (DoctorApplication.mUser.getType() == 2) {
                        hint = "请输入昵称";
                    }else if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                            ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                        hint = "请输入简称";
                    }

                } else if ("sex".equals(mType)) {
                    showSingleSexDialog();
                } else if ("hospital".equals(mType)) {
                    getAllHospital();
                } else if ("department".equals(mType)) {
                    getAllDepartment();
                } else if ("professional".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("phone".equals(mType)) {
                    hint = "请输入联系手机";
                } else if ("goodAt".equals(mType)) {
                    hint = "请输入擅长";
                } else if ("intro".equals(mType)) {
                    hint = "请输入简介";
                } else if ("duty".equals(mType)) {

                    if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                            ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                        hint = "请输入专业";
                    }else{
                        hint = "请输入职务";
                    }

                } else if ("phoneAdv".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("orderAdv".equals(mType)) {
                } else if ("noteAdv".equals(mType)) {
                } else if ("city".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("area".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("communityName".equals(mType)) {
                    hint = "请输入社区名称";
                }else if ("addressDetail".equals(mType)) {
                    hint = "请输入详细地址";
                } else if ("birthday".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("true_name".equals(mType)) {
                if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                        ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                        hint = "请输入工商局注册名字";
                    }else{
                        hint = "请输入真实姓名";
                    }
                    mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                } else if ("province".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("normalAdc".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("bigAdc".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("familyAdc".equals(mType)) {
                    UserInfoItemView.this.onClick();
                } else if ("healthAdc".equals(mType)) {
                    UserInfoItemView.this.onClick();
                }else if("hospitalGrade".equals(mType)){
                    UserInfoItemView.this.onClick();
                }else if("category".equals(mType)){
                    UserInfoItemView.this.onClick();
                }

                if (!StringUtils.isEmpty(hint)) {
                    showDialog(hint);
                }

            }
        });

    }

    private void showDialog(String hint) {

        String content = mContent.getText().toString().trim();
        String title = mTitle.getText().toString().trim();
        if ("暂无".equals(content)) {
            content = "";
        }

        boolean wrapInScrollView = true;

        final EditText view = (EditText) mActivity.getLayoutInflater().inflate(R.layout.custom_view, null);
        view.setText(content);
        view.setSelection(content.length());
        view.setHint(hint);

        if ("phone".equals(mType)) {
            view.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if ("name".equals(mType) || "true_name".equals(mType)) {
            mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }

        new MaterialDialog.Builder(mActivity)
                .title(title)
                .customView(view, wrapInScrollView)
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

    private void getAllDepartment() {
        mActivity.showDialog();

        String url = HttpAddress.GET_ALL_DEPARTMENT;
        if (mHospitalType == 2) {
            url = HttpAddress.GET_DEPARTMENT_GRADE;
        }

        HttpUtils.postDataFromServer(url, "%5%", new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                mActivity.dismissDialog();
                mActivity.showToast("获取信息失败");
            }

            @Override
            public void onSuccess(String data) {
                mActivity.dismissDialog();
                List<AddDepartments> doctors = JSONObject.parseArray(data, AddDepartments.class);

                showDepartmentDialog(doctors);
            }
        });
    }

    private void showDepartmentDialog(final List<AddDepartments> list) {
        String title = mTitle.getText().toString().trim();

        int position = -1;

        for (int i = 0; i < list.size(); i++) {
            if (mInfo.getDepartmentId() == list.get(i).getId()) {
                position = i;
            }
        }

        new MaterialDialog.Builder(mActivity)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        mInfo.setDepartmentId(list.get(which).getId());
                        mInfo.setDepartmentName(String.valueOf(text));
                        mContent.setText(String.valueOf(text));
                        return false;
                    }
                })
                .show();
    }

    private void getAllHospital() {

        Intent intent = mActivity.getNewIntent(SelectHospitalActivity.class);
        mActivity.startActivityForResult(intent, 16);

//        mActivity.showDialog();
//        HttpUtils.postDataFromServer(HttpAddress.GET_ALL_HOSPITAL, new NetworkResponseHandler() {
//            @Override
//            public void onFail(String message) {
//                mActivity.dismissDialog();
//                mActivity.showToast("获取信息失败");
//            }
//
//            @Override
//            public void onSuccess(String data) {
//                mActivity.dismissDialog();
//                List<HospitalDetailInfo> list = JSONObject.parseArray(data, HospitalDetailInfo.class);
//                showHospitalDialog(list);
//            }
//        });
    }

    private void showHospitalDialog(final List<HospitalDetailInfo> list) {
        String title = mTitle.getText().toString().trim();

        int position = -1;

        for (int i = 0; i < list.size(); i++) {
            if (mInfo.getHospitalId() == list.get(i).getId()) {
                position = i;
            }
        }

        new MaterialDialog.Builder(mActivity)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        mInfo.setHospitalId(list.get(which).getId());
                        mInfo.setHospitalName(String.valueOf(text));
                        mContent.setText(String.valueOf(text));
                        return false;
                    }
                })
                .show();
    }

    private void showSingleSexDialog() {

        String title = mTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        int position = -1;

        if (content.contains("男")) {
            position = 0;
        } else if (content.contains("女")) {
            position = 1;
        }

        new MaterialDialog.Builder(mActivity)
                .title(title)
                .items(R.array.items_sex)
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        if (which == 0) {
                            mInfo.setUserSex(1);
                        } else if (which == 1) {
                            mInfo.setUserSex(2);
                        } else if (which == -1) {
                            mActivity.showToast("请选择性别");
                            showSingleSexDialog();
                        }

                        initData(mType);

                        return true;
                    }
                })
                .show();
    }

    private void updateContent(String content) {
        if ("name".equals(mType)) {
            if (DoctorApplication.mUser.getType() == 1) {
                mInfo.setNickName(content);
                mInfo.setTrueName(content);
            } else {
                mInfo.setNickName(content);
            }
            setContent(mInfo.getNickName());
        } else if ("professional".equals(mType)) {
            setContent(content);
        } else if ("phone".equals(mType)) {

            if (!StringUtils.isEmpty(DoctorApplication.mUser.getUserName())) {
                mInfo.setPhone(DoctorApplication.mUser.getUserName());
                setContent(DoctorApplication.mUser.getUserName());
            }

        } else if ("birthday".equals(mType)) {
            mInfo.setBrithday(content);
            setContent(content);
        } else if ("goodAt".equals(mType)) {
            mInfo.setGoodAt(content);
            setContent(content);
        } else if ("intro".equals(mType)) {
            mInfo.setIntro(content);
            setContent(content);
        } else if ("duty".equals(mType)) {
            mInfo.setDuty(content);
            setContent(content);
        } else if ("phoneAdv".equals(mType)) {
            mInfo.setPhoneAdvisory(content);
            setContent(content);
        } else if ("orderAdv".equals(mType)) {
            mInfo.setOrderAadvisory(content);
            setContent(content);
        } else if ("noteAdv".equals(mType)) {
            mInfo.setNoteOrderAdvisory(content);
            setContent(content);
        } else if ("city".equals(mType)) {
            setTitle("城市");
            setContent(mInfo.getCity());
        } else if ("area".equals(mType)) {
            setTitle("区域");
            setContent(mInfo.getArea());
        }else if ("communityName".equals(mType)) {
            setTitle("请输入社区名称");
            mInfo.setCommunityName(content);
            setContent(content);
        } else if ("addressDetail".equals(mType)) {
            setTitle("请输入详细地址");
            mInfo.setDetailAddress(content);
            setContent(content);
        } else if ("province".equals(mType)) {
            setContent(mInfo.getProvince());
            setTitle("省份");
        } else if ("true_name".equals(mType)) {
            mInfo.setTrueName(content);
            setContent(content);
        } else if ("normalAdc".equals(mType)) {
            setTitle("普通咨询");
            setContent(content);
            mInfo.setNormalAdvisory(content);
        } else if ("bigAdc".equals(mType)) {
            setTitle("大病会诊");
            mInfo.setBigAdvisory(content);
            setContent(content);
        } else if ("familyAdc".equals(mType)) {
            setTitle("家庭医生");
            mInfo.setFamilyDoctor(content);
            setContent(content);
        } else if ("healthAdc".equals(mType)) {
            setTitle("健康管理");
            mInfo.setHealthManager(content);
            setContent(content);
        } else if ("category".equals(mType)) {
            setTitle("所属分类");
            mInfo.setDepartmentName(content);
            setContent(content);
        }
    }

    private void initData(String type) {
        if ("name".equals(type)) {
            if (DoctorApplication.mUser.getType() == 1) {
                setTitle("姓名");
            } else if (DoctorApplication.mUser.getType() == 2) {
                setTitle("昵称");
            }if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                    ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                setTitle("简称");
            }
            mContent.setInputType(InputType.TYPE_CLASS_PHONE);
            mContent.setText(mInfo.getNickName());
        } else if ("true_name".equals(type)) {
             if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                setTitle("全称");
            }else {
                setTitle("姓名");
            }
            mContent.setText(mInfo.getTrueName());
        } else if ("sex".equals(type)) {
            int sex = mInfo.getUserSex();
            setTitle("性别");
            if (sex == 0) {
                mContent.setText("暂无");
            } else if (sex == 2) {
                setContent("女");
            } else {
                setContent("男");
            }
        } else if ("hospital".equals(type)) {
            setTitle("所在医院");
            setContent(mInfo.getHospitalName());
        } else if ("hospitalGrade".equals(type)) {
            setTitle("医院等级");
            setContent(mInfo.getHospitalGrade());
        } else if ("department".equals(type)) {
            setTitle("所在科室");
            setContent(mInfo.getDepartmentName());
        } else if ("professional".equals(type)) {
            setTitle("职称");
            setContent(mInfo.getProfessionalTitle());
        } else if ("phone".equals(type)) {

            if (!StringUtils.isEmpty(DoctorApplication.mUser.getUserName())) {
                setContent(DoctorApplication.mUser.getUserName());
            }

            setTitle("注册手机号");
        } else if ("birthday".equals(mType)) {
            setTitle("出生日期");
            setContent(mInfo.getBrithday());
        } else if ("goodAt".equals(type)) {
            setContent(mInfo.getGoodAt());
            setTitle("擅长");
        } else if ("intro".equals(type)) {
            setTitle("简介");
            setContent(mInfo.getIntro());
        } else if ("duty".equals(type)) {
            setContent(mInfo.getDuty());

            if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                    ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                setTitle("专业");
            }else{
                setTitle("职务");
            }

        } else if ("phoneAdv".equals(type)) {
            setContent(mInfo.getPhoneAdvisory());
            setTitle("电话咨询费用（单位 元）");
        } else if ("orderAdv".equals(type)) {
            setContent(mInfo.getOrderAadvisory());
            setTitle("预约就诊费用（单位 元）");
        } else if ("noteAdv".equals(type)) {
            setContent(mInfo.getNoteOrderAdvisory());
            setTitle("短信咨询费用（单位 元）");
        } else if ("city".equals(mType)) {
            setTitle("城市");
            setContent(mInfo.getCity());
        } else if ("area".equals(mType)) {
            setTitle("区域");
            setContent(mInfo.getArea());
        } else if ("communityName".equals(mType)) {
            setTitle("请输入社区名称");
            setContent(mInfo.getCommunityName());
        } else if ("addressDetail".equals(mType)) {
            setTitle("请输入详细地址");
            setContent(mInfo.getDetailAddress());
        } else if ("province".equals(mType)) {
            setContent(mInfo.getProvince());
            setTitle("省份");
        } else if ("normalAdc".equals(mType)) {
            setTitle("普通咨询费用（单位 元）");
            setContent(mInfo.getNormalAdvisory());
        } else if ("bigAdc".equals(mType)) {
            setTitle("大病会诊费用（单位 元）");
            setContent(mInfo.getBigAdvisory());
        } else if ("familyAdc".equals(mType)) {
            setTitle("家庭医生费用（单位 元）");
            setContent(mInfo.getFamilyDoctor());
        } else if ("healthAdc".equals(mType)) {
            setTitle("健康管理费用（单位 元）");
            setContent(mInfo.getHealthManager());
        }else if("category".equals(mType)){
            setTitle("所在分类");
            setContent(mInfo.getDepartmentName());
        }
    }

    private void setTitle(String title) {
        mTitle.setText(title);
    }

    private void setContent(String title) {

        if (StringUtils.isEmpty(title)) {
            mContent.setText("暂无");
        } else {
            mContent.setText(title);
        }

    }


    private void initView(View view) {
        mTitle = (TextView) view.findViewById(R.id.title);
        mContent = (TextView) view.findViewById(R.id.content);
        mEdit = (TextView) view.findViewById(R.id.edit);
        mEdit.setVisibility(View.GONE);
    }

    public void initViewVisiable(boolean isVisiable) {

        if ("hospitalGrade".equals(mType)) {

            if (mIsPass == 0) {
                if (isVisiable) {
                    mEdit.setVisibility(View.VISIBLE);
                } else {
                    mEdit.setVisibility(View.GONE);
                }
            }

        } else if ("phone".equals(mType)) {
            mEdit.setVisibility(View.GONE);
        } else {
            if (isVisiable) {
                mEdit.setVisibility(View.VISIBLE);
            } else {
                mEdit.setVisibility(View.GONE);
            }
        }

    }

    public boolean backPress() {
        if (mEdit.getVisibility() == View.VISIBLE) {
            mEdit.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    public String getContent() {
        return mContent.getText().toString().trim();
    }

    public void setContent() {
        mContent.setText("");

        if ("city".equals(mType)) {
            mInfo.setCity("");
        } else if ("area".equals(mType)) {
            mInfo.setArea("");
        } else if ("province".equals(mType)) {
            mInfo.setProvince("");
        }
    }

    public void show() {
        if ("city".equals(mType)) {
            String content = mItemView.getContent();
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请先选择省份");
            } else {
                final String content1 = getContent();

                new SelectCityDialog(mActivity, 1, content, false, false, new SelectCityDialog.OnSelectListener() {
                    @Override
                    public void onSelect(String city) {
                        mContent.setText(city);
                        if (!city.equals(content1)) {
                            mActivity.initArea();
                            mActivity.selectArea();
                        }
                    }
                },0);

            }
        } else if ("area".equals(mType)) {

            String content = mItemView.getContent();
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请先选择所在市");
            } else {
                new SelectCityDialog(mActivity, 2, content, false, false, new SelectCityDialog.OnSelectListener() {
                    @Override
                    public void onSelect(String city) {
                        mContent.setText(city);
                    }
                },0);
            }

        }
    }

    public void onClick() {
        if ("area".equals(mType)) {

            String content = mItemView.getContent();
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请先选择所在市");
            } else {
                new SelectCityDialog(mActivity, 2, content, false, false, new SelectCityDialog.OnSelectListener() {
                    @Override
                    public void onSelect(String city) {
                        mContent.setText(city);
                    }
                },0);
            }

        } else if ("city".equals(mType)) {
            String content = mItemView.getContent();
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请先选择省份");
            } else {
                final String content1 = getContent();

                new SelectCityDialog(mActivity, 1, content, false, false, new SelectCityDialog.OnSelectListener() {
                    @Override
                    public void onSelect(String city) {
                        mContent.setText(city);
                        if (!city.equals(content1)) {
                            mActivity.initArea();
                            mActivity.selectArea();
                        }
                    }
                },0);

            }
        } else if ("province".equals(mType)) {

            new SelectCityDialog(mActivity, 0, "", false, false, new SelectCityDialog.OnSelectListener() {
                @Override
                public void onSelect(String city) {
                    final String content = getContent();
                    mContent.setText(city);
                    if (!city.equals(content)) {
                        mActivity.initCityAndArea();
                        mActivity.selectCity();
                    }
                }
            },0);
        } else if ("sex".equals(mType)) {
            showSingleSexDialog();
        } else if ("normalAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("bigAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("familyAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("healthAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("professional".equals(mType)) {
            showSelectProfessionalDialog();
        } else if ("phoneAdv".equals(mType)) {
            showSelectPriceDialog();
        } else if ("hospitalGrade".equals(mType)) {
            showSelectHospitalGrade();
        } else if ("birthday".equals(mType)) {
            showSelectBirthday();
        }else if("category".equals(mType)){
            showSelectCategory();
        }
    }


    private void showSelectCategory() {

        new MaterialDialog.Builder(mActivity)
                .title("选择分类")
                .items(R.array.category_type)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if(which==0){
                            getCategory("7");
                        }else{
                            getCategory("6");
                        }
                    }
                })
                .show();

    }

    private void getCategory(String count) {
        mActivity.showDialog();
        HttpUtils.postDataFromServer(HttpAddress.GET_DEPARTMENT_GRADE,"%"+count+"%", new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                mActivity.showToast("获取失败");
            }

            @Override
            public void onSuccess(String data) {
                mActivity.dismissDialog();
                if(!StringUtils.isEmpty(data)){
                    List<AddDepartments> list = JSONObject.parseArray(data, AddDepartments.class);
                    showCategory(list);
                }

            }
        });
    }

    private void showCategory(final List<AddDepartments> list) {
        String title = mTitle.getText().toString().trim();

        int position = -1;

        for (int i = 0; i < list.size(); i++) {
            if (mInfo.getDepartmentId() == list.get(i).getId()) {
                position = i;
            }
        }

        new MaterialDialog.Builder(mActivity)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        mInfo.setDepartmentId(list.get(which).getId());
                        mInfo.setDepartmentName(String.valueOf(text));
                        mContent.setText(String.valueOf(text));
                        return false;
                    }
                })
                .show();
    }

    public static final String DATEPICKER_TAG = "datepicker";

    private void showSelectBirthday() {

        final Calendar calendar = Calendar.getInstance();

        String birthday = mInfo.getBrithday();

        if (!StringUtils.isEmpty(birthday)) {
            String[] arr = birthday.split("-");
            Integer year = Integer.parseInt(arr[0]);
            Integer month = Integer.parseInt(arr[1]) - 1;
            Integer day = Integer.parseInt(arr[2]);

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }


        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                String birthday = year + "-" + (month + 1) + "-" + day + "";
                mInfo.setBrithday(birthday);
                setContent(birthday);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
        datePickerDialog.setVibrate(true);
        datePickerDialog.setYearRange(1930, 2028);
        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.show(mActivity.getSupportFragmentManager(), DATEPICKER_TAG);

    }

    private void showSelectHospitalGrade() {
        new MaterialDialog.Builder(mActivity)
                .title("选择医院等级")
                .items(R.array.hospital_grade)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String str = String.valueOf(text);
                        mContent.setText(str);
                    }
                })
                .show();
    }

    private void showSelectProfessionalDialog() {
        Resources res = mActivity.getResources();
        int resourceId = res.getIdentifier(mType, "array", mActivity.getPackageName());

        new MaterialDialog.Builder(mActivity)
                .title("选择职称")
                .items(resourceId)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        dialog.dismiss();
                        mContent.setText(text);
                        mInfo.setProfessionalTitle(String.valueOf(text));
                    }
                })
                .show();
    }

    private void showSelectPriceDialog() {
        Resources res = mActivity.getResources();
        int resourceId = res.getIdentifier(mType, "array", mActivity.getPackageName());

        new MaterialDialog.Builder(mActivity)
                .title("选择要设置的诊疗费")
                .content("单位（元）")
                .items(resourceId)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        dialog.dismiss();
                        String content = String.valueOf(text);
                        mContent.setText(text);

                        if ("normalAdc".equals(mType)) {
                            mInfo.setNormalAdvisory(content);
                        } else if ("bigAdc".equals(mType)) {
                            mInfo.setBigAdvisory(content);
                        } else if ("familyAdc".equals(mType)) {
                            mInfo.setFamilyDoctor(content);
                        } else if ("healthAdc".equals(mType)) {
                            mInfo.setHealthManager(content);
                        } else if ("phoneAdv".equals(mType)) {
                            mInfo.setPhoneAdvisory(content);
                        }

                    }
                })
                .show();

    }

    public boolean isVisiable() {
        return mEdit.getVisibility() == View.VISIBLE;
    }

    public UserInfo getUserInfo(UserInfo info) {
        String content = getContent();
        if ("name".equals(mType)) {

            if (DoctorApplication.mUser.getType() == 2) {
                if (StringUtils.isEmpty(content)) {
                    mActivity.showToast("不能为空");
                    return null;
                } else {
                    info.setNickName(content);
                }
            } else if (DoctorApplication.mUser.getType() == 1) {
                if (StringUtils.isEmpty(content)) {
                    mActivity.showToast("真实姓名不能为空");
                    return null;
                } else {
                    info.setNickName(content);
                    info.setTrueName(content);
                }
            }else{
                if (StringUtils.isEmpty(content)) {
                    mActivity.showToast("不能为空");
                    return null;
                } else {
                    info.setNickName(content);
                }
            }


        } else if ("true_name".equals(mType)) {
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("真实姓名不能为空");
                return null;
            } else {
                info.setTrueName(content);
            }
        } else if ("sex".equals(mType)) {

            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("没有选择性别");
                return null;
            } else {
                if (content.contains("男")) {
                    info.setUserSex(1);
                } else if (content.contains("女")) {
                    info.setUserSex(2);
                }
            }

        } else if ("hospital".equals(mType)) {
            if (StringUtils.isEmpty(mInfo.getHospitalName())) {
                mActivity.showToast("没有选择所在医院");
            } else {
                info.setHospitalName(mInfo.getHospitalName());
                info.setHospitalId(mInfo.getHospitalId());
            }
        } else if ("hospitalGrade".equals(mType)) {
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请选择医院等级");
                return null;
            } else {
                info.setHospitalGrade(content);
            }
        } else if ("department".equals(mType)) {

            if (StringUtils.isEmpty(mInfo.getHospitalName())) {
                mActivity.showToast("没有选择所在科室");
                return null;
            } else {
                info.setDepartmentName(mInfo.getDepartmentName());
                info.setDepartmentId(mInfo.getDepartmentId());
            }

        } else if ("professional".equals(mType)) {
            if (StringUtils.isEmpty(mInfo.getHospitalName())) {
                mActivity.showToast("职称不能为空");
                return null;
            } else {
                info.setProfessionalTitle(content);
            }
        } else if ("phone".equals(mType)) {

            if (!StringUtils.isTelActive(content)) {
                mActivity.showToast("手机号格式错误");
                return null;
            } else {
                info.setPhone(content);
            }
        } else if ("goodAt".equals(mType)) {
            info.setGoodAt(mInfo.getGoodAt());
        } else if ("intro".equals(mType)) {
            info.setIntro(mInfo.getIntro());
        } else if ("duty".equals(mType)) {


            if(StringUtils.isEmpty(content)){

                if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                        ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                    mActivity.showToast("请输入专业");
                }else{
                    mActivity.showToast("请输入职务");
                }

                return null;
            }else{
                info.setDuty(mInfo.getDuty());
            }

        } else if ("phoneAdv".equals(mType)) {
            info.setPhoneAdvisory(content);
        } else if ("orderAdv".equals(mType)) {
            info.setOrderAadvisory(content);
        } else if ("noteAdv".equals(mType)) {
            info.setNoteOrderAdvisory(content);
        } else if ("city".equals(mType)) {

            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("没有选择城市");
                return null;
            } else {
                info.setCity(content);
            }

        } else if ("area".equals(mType)) {

            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("没有选择地区");
                return null;
            } else {
                info.setArea(content);
            }

        }  else if ("communityName".equals(mType)) {
            info.setCommunityName(content);
        }else if ("addressDetail".equals(mType)) {
            info.setDetailAddress(content);
        } else if ("province".equals(mType)) {

            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("没有选择省份");
                return null;
            } else {
                info.setProvince(content);
            }

        } else if ("normalAdc".equals(mType)) {
            info.setNormalAdvisory(content);
        } else if ("bigAdc".equals(mType)) {
            info.setBigAdvisory(content);
        } else if ("familyAdc".equals(mType)) {
            info.setFamilyDoctor(content);
        } else if ("healthAdc".equals(mType)) {
            info.setHealthManager(content);
        } else if ("birthday".equals(mType)) {
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请选择出生日期");
                return null;
            } else {
                info.setBrithday(content);
            }
        }else if("category".equals(mType)){
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请选择所在分类");
                return null;
            } else {
                info.setDepartmentName(mInfo.getDepartmentName());
                info.setDepartmentId(mInfo.getDepartmentId());
            }
        }
        return info;
    }

    public void setUserInfo(UserInfo info) {
        mInfo = info;
        initData(mType);
    }

    public void setHospitalInfo(int id, String hospital_name) {

        mInfo.setHospitalId(id);
        mInfo.setHospitalName(hospital_name);
        mContent.setText(hospital_name);
    }

    public void setHospitalGrade(int is_pass, String grade, int hospital_type) {
        mIsPass = is_pass;
        mContent.setText(grade);
        mHospitalType = hospital_type;
        if (is_pass == 0 && hospital_type == 1) {
            mEdit.setVisibility(View.VISIBLE);
        } else {
            mEdit.setVisibility(View.GONE);
        }
    }

    public int getmHospitalType() {
        return mHospitalType;
    }

    public void setmHospitalType(int mHospitalType) {
        this.mHospitalType = mHospitalType;
    }
}
