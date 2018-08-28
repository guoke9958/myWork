package com.cn.xa.qyw.view;

import android.content.Intent;
import android.content.res.Resources;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.cn.xa.qyw.ui.userinfo.AddUserInfoActivity;
import com.cn.xa.qyw.utils.DensityUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

/**
 * Created by 409160 on 2016/12/5.
 */
public class AddUserInfoItemView implements View.OnClickListener {

    private boolean mIsShowEdit;
    private String mType;
    private AddUserInfoActivity mActivity;
    private AddUserInfoItemView mItemView;
    private TextView mTitle;
    private EditText mContent;
    private TextView mEdit;
    private int mDepartmentId;
    private int mHospitalId;
    private int mHospitalPass;
    private int mHospitalType;
    private int mCategoryId;

    public AddUserInfoItemView(AddUserInfoActivity activity, LinearLayout parent, String type, boolean isShowEdit) {
        mType = type;
        mActivity = activity;
        mIsShowEdit = isShowEdit;
        LayoutInflater mInflater = activity.getLayoutInflater();

        View view = mInflater.inflate(R.layout.item_add_user_info, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        initView(view);

        initData();
        initData(type);

        if ("goodAt".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        } else if ("intro".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        }

        parent.addView(view, lp);
    }

    public AddUserInfoItemView(AddUserInfoActivity activity, LinearLayout parent, String type, boolean isShowEdit, AddUserInfoItemView itemView) {
        mItemView = itemView;
        mType = type;
        mActivity = activity;
        mIsShowEdit = isShowEdit;
        LayoutInflater mInflater = activity.getLayoutInflater();

        View view = mInflater.inflate(R.layout.item_add_user_info, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        initView(view);

        initData();
        initData(type);

        if ("goodAt".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        } else if ("intro".equals(mType)) {
            mContent.setMinHeight(DensityUtils.dip2px(activity, 100));
        }

        parent.addView(view, lp);
    }

    private void initData() {
        String hint = "";
        if ("name".equals(mType)) {

            if (DoctorApplication.mUser.getType() == 3 || DoctorApplication.mUser.getType() == 4
                    || DoctorApplication.mUser.getType() == 5 || DoctorApplication.mUser.getType() == 6) {
                hint = "请输入简称";
            } else {
                hint = "请输入昵称";
            }
            mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else if ("sex".equals(mType)) {
            hint = "选择性别";
            mContent.setFocusable(false);
        } else if ("hospital".equals(mType)) {
            hint = "选择医院";
            mContent.setFocusable(false);
        } else if ("hospitalGrade".equals(mType)) {
            hint = "选择医院等级";
            mContent.setEnabled(false);
            mContent.setFocusable(false);
        } else if ("department".equals(mType)) {
            hint = "选择科室";
            mContent.setFocusable(false);
        } else if ("birthday".equals(mType)) {
            hint = "选择出生日期";
            mContent.setFocusable(false);
        } else if ("professional".equals(mType)) {
            mContent.setFocusable(false);
            hint = "请选择职称";
        } else if ("phone".equals(mType)) {
            hint = "请输入联系手机";
            mContent.setInputType(InputType.TYPE_CLASS_PHONE);

            if (StringUtils.isEmpty(DoctorApplication.mUser.getUserName())) {
                mContent.setText(DoctorApplication.mUser.getUserName());
            }

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
            mContent.setFocusable(false);
            hint = "请选择手机咨询费用";
        } else if ("orderAdv".equals(mType)) {
            hint = "请输入预约就诊费用";
        } else if ("noteAdv".equals(mType)) {
            hint = "请输入短信预约费用";
        } else if ("true_name".equals(mType)) {
            if (DoctorApplication.mUser.getType() == 3 || DoctorApplication.mUser.getType() == 4
                    || DoctorApplication.mUser.getType() == 5 || DoctorApplication.mUser.getType() == 6) {
                hint = "请输入工商局注册名字";
            } else {
                hint = "请输入真实姓名";
            }
            mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        } else if ("province".equals(mType)) {
            mContent.setFocusable(false);
            hint = "选择所在省份";
        } else if ("city".equals(mType)) {
            mContent.setFocusable(false);
            hint = "选择所在城市";
        } else if ("area".equals(mType)) {
            mContent.setFocusable(false);
            hint = "选择所在区";
        } else if ("addressDetail".equals(mType)) {
            hint = "请输入详细地址";
        } else if ("normalAdc".equals(mType)) {
            hint = "请选择普通咨询费用";
            mContent.setFocusable(false);
        } else if ("bigAdc".equals(mType)) {
            hint = "请选择需要大病会诊费用";
            mContent.setFocusable(false);
        } else if ("familyAdc".equals(mType)) {
            mContent.setFocusable(false);
            hint = "请选择作为家庭医生费用";
        } else if ("healthAdc".equals(mType)) {
            mContent.setFocusable(false);
            hint = "请选择健康管理的费用";
        } else if ("category".equals(mType)) {
            mContent.setFocusable(false);
            hint = "请选择所属分类";
        }

        if (!StringUtils.isEmpty(hint)) {
            mContent.setHint(hint);
        }
    }

    private void initData(String type) {
        if ("name".equals(type)) {

            if (DoctorApplication.mUser.getType() == 3 || DoctorApplication.mUser.getType() == 4
                    || DoctorApplication.mUser.getType() == 5 || DoctorApplication.mUser.getType() == 6) {
                setTitle("简称");
            } else {
                setTitle("昵称");
            }

        } else if ("true_name".equals(type)) {
            if (DoctorApplication.mUser.getType() == 3 || DoctorApplication.mUser.getType() == 4
                    || DoctorApplication.mUser.getType() == 5 || DoctorApplication.mUser.getType() == 6) {
                setTitle("全称");
            } else {
                setTitle("姓名");
            }
        } else if ("sex".equals(type)) {
            setTitle("性别");
        } else if ("hospital".equals(type)) {
            setTitle("所在医院");
        } else if ("department".equals(type)) {
            setTitle("所在科室");
        } else if ("hospitalGrade".equals(type)) {
            setTitle("医院等级");
        } else if ("professional".equals(type)) {
            setTitle("职称");
        } else if ("birthday".equals(type)) {
            setTitle("出生日期");
        } else if ("phone".equals(type)) {

            if (!StringUtils.isEmpty(DoctorApplication.mUser.getUserName())) {
                mContent.setText(DoctorApplication.mUser.getUserName());
            }

            setTitle("注册手机号");
        } else if ("goodAt".equals(type)) {
            setTitle("擅长");
        } else if ("intro".equals(type)) {
            setTitle("简介");
        } else if ("duty".equals(type)) {

            if (DoctorApplication.mUser.getType() == 3||DoctorApplication.mUser.getType() == 4
                    ||DoctorApplication.mUser.getType() == 5||DoctorApplication.mUser.getType() == 6) {
                setTitle("专业");
            }else{
                setTitle("职务");
            }

        } else if ("phoneAdv".equals(type)) {
            setTitle("电话咨询费用（单位：元）");
        } else if ("orderAdv".equals(type)) {
            setTitle("预约就诊费用");
        } else if ("noteAdv".equals(type)) {
            setTitle("短信咨询费用");
        } else if ("city".equals(mType)) {
            setTitle("城市");
        } else if ("area".equals(mType)) {
            setTitle("区域");
        } else if ("addressDetail".equals(mType)) {
            setTitle("请输入详细地址");
        } else if ("province".equals(mType)) {
            setTitle("省份");
        } else if ("normalAdc".equals(mType)) {
            setTitle("普通咨询费用（单位：元）");
        } else if ("bigAdc".equals(mType)) {
            setTitle("大病会诊费用（单位：元）");
        } else if ("familyAdc".equals(mType)) {
            setTitle("家庭医生费用（单位：元）");
        } else if ("healthAdc".equals(mType)) {
            setTitle("健康管理费用（单位：元）");
        } else if ("category".equals(mType)) {
            setTitle("请选择所属分类");
        }
    }

    private void setTitle(String title) {
        mTitle.setText(title);
    }


    private void initView(View view) {
        mTitle = (TextView) view.findViewById(R.id.title);
        mContent = (EditText) view.findViewById(R.id.content);
        mEdit = (TextView) view.findViewById(R.id.edit);
        mContent.setOnClickListener(this);

        if (mIsShowEdit) {
            mEdit.setVisibility(View.VISIBLE);
        } else
            mEdit.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
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
                }, 0);
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
                }, 0);

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
            }, 0);
        } else if ("sex".equals(mType)) {
            showSingleSexDialog();
        } else if ("hospital".equals(mType)) {
            getAllHospital();
        } else if ("department".equals(mType)) {
            getAllDepartment();
        } else if ("normalAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("bigAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("familyAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("healthAdc".equals(mType)) {
            showSelectPriceDialog();
        } else if ("phoneAdv".equals(mType)) {
            showSelectPriceDialog();
        } else if ("professional".equals(mType)) {
            showSelectProfessionalDialog();
        } else if ("hospitalGrade".equals(mType)) {
            showSelectHospitalGrade();
        } else if ("birthday".equals(mType)) {
            showSelectBirthday();
        } else if ("category".equals(mType)) {
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
        HttpUtils.postDataFromServer(HttpAddress.GET_DEPARTMENT_GRADE, "%" + count + "%", new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                mActivity.showToast("获取失败");
            }

            @Override
            public void onSuccess(String data) {
                mActivity.dismissDialog();
                if (!StringUtils.isEmpty(data)) {
                    List<AddDepartments> list = JSONObject.parseArray(data, AddDepartments.class);
                    showCategory(list);
                }

            }
        });
    }

    private void showCategory(final List<AddDepartments> list) {
        String title = mTitle.getText().toString().trim();

        int position = 0;

        new MaterialDialog.Builder(mActivity)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mCategoryId = list.get(which).getId();
                        mContent.setText(String.valueOf(text));
                        return false;
                    }
                })
                .show();
    }

    public static final String DATEPICKER_TAG = "datepicker";

    private void showSelectBirthday() {

        final Calendar calendar = Calendar.getInstance();


        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                String birthday = year + "-" + (month + 1) + "-" + day + "";
                mContent.setText(birthday);
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
                        mContent.setText(text);
                    }
                })
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

        int position = 0;


        new MaterialDialog.Builder(mActivity)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mDepartmentId = list.get(which).getId();
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

        int position = 0;


        new MaterialDialog.Builder(mActivity)
                .title(title)
                .items(list)
                .itemsCallbackSingleChoice(position, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mHospitalId = list.get(which).getId();
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
                            mContent.setText("男");
                        } else if (which == 1) {
                            mContent.setText("女");
                        } else if (which == -1) {
                            mActivity.showToast("请选择性别");
                            showSingleSexDialog();
                        }
                        return true;
                    }
                })
                .show();
    }

    public String getContent() {
        return mContent.getText().toString().trim();
    }

    public void setContent() {
        mContent.setText("");
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
                }, 0);

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
                }, 0);
            }

        }
    }

    public UserInfo setUserInfo(UserInfo info) {
        String content = getContent();
        if ("name".equals(mType)) {

            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("名称不能为空");
                return null;
            } else {
                info.setNickName(content);
            }

        } else if ("true_name".equals(mType)) {
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("名称不能为空");
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
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请选择所在医院");
                return null;
            } else {
                info.setHospitalId(mHospitalId);
                info.setHospitalName(content);
            }

        } else if ("hospitalGrade".equals(mType)) {
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请选择医院等级");
                return null;
            } else {
                info.setHospitalGrade(content);
            }
        } else if ("department".equals(mType)) {

            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请选择所在科室");
                return null;
            } else {
                info.setDepartmentId(mDepartmentId);
            }

        } else if ("professional".equals(mType)) {

            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请输入您当前职称");
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
            info.setGoodAt(content);
        } else if ("intro".equals(mType)) {
            info.setIntro(content);
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
                info.setDuty(content);
            }

        } else if ("phoneAdv".equals(mType)) {
            info.setPhoneAdvisory(content);
        } else if ("orderAdv".equals(mType)) {
        } else if ("noteAdv".equals(mType)) {
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

        } else if ("addressDetail".equals(mType)) {
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
        } else if ("category".equals(mType)) {
            if (StringUtils.isEmpty(content)) {
                mActivity.showToast("请选择所在分类");
                return null;
            } else {
                info.setDepartmentId(mCategoryId);
                info.setDepartmentName(content);
            }
        }
        return info;
    }

    public void setHospitalInfo(int id, String hospital_name) {
        mHospitalId = id;
        mContent.setText(hospital_name);
    }

    public void setHospitalGrade(int hospitalPass, String grade, int hospital_type) {
        mHospitalPass = hospitalPass;
        mContent.setText(grade);
        mHospitalType = hospital_type;
        if (mHospitalPass == 0 && hospital_type == 1) {
            mContent.setEnabled(true);
        } else
            mContent.setEnabled(false);

    }

    public int getmHospitalType() {
        return mHospitalType;
    }

    public void setmHospitalType(int mHospitalType) {
        this.mHospitalType = mHospitalType;
    }


}
