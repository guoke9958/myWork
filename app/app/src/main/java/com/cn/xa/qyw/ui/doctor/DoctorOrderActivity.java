package com.cn.xa.qyw.ui.doctor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.OrderMsg;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.utils.StringUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by 409160 on 2017/1/4.
 */
public class DoctorOrderActivity extends DoctorBaseActivity implements TextWatcher {

    private EditText mOrderInfo;
    private EditText mPhone;
    private Button mBtnSend;
    private String mPhoneNumber;
    private EditText mTrueName;
    private String mDoctorId;
    private String mDoctorName;
    private EditText mUserAge;
    private EditText mUserAddress;
    private String mGrade;
    private View mAgeParent;
    private View mAddressParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("短信预约");
        mGrade = getIntent().getStringExtra("grade");
        mPhoneNumber = getIntent().getStringExtra("phone");
        mDoctorId = getIntent().getStringExtra("doctorid");
        mDoctorName = getIntent().getStringExtra("doctorName");
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mOrderInfo = (EditText) findViewById(R.id.order_info);
        mPhone = (EditText) findViewById(R.id.user_phone);
        mBtnSend = (Button) findViewById(R.id.order_btn);
        mTrueName = (EditText) findViewById(R.id.user_name);
        mUserAge = (EditText) findViewById(R.id.user_age);
        mUserAddress = (EditText) findViewById(R.id.user_address);
        mAgeParent = findViewById(R.id.age_parent);
        mAddressParent = findViewById(R.id.address_parent);

        if (StringUtils.isEmpty(mGrade)) {
            mAgeParent.setVisibility(View.GONE);
            mAddressParent.setVisibility(View.GONE);
        } else {

            if(mGrade.contains("生活") || mGrade.contains("医疗") || mGrade.contains("学校")){
                mAgeParent.setVisibility(View.GONE);
                mAddressParent.setVisibility(View.GONE);
            }

            mAgeParent.setVisibility(View.VISIBLE);
            mAddressParent.setVisibility(View.VISIBLE);
        }

    }

    private void initListener() {
        mOrderInfo.addTextChangedListener(this);
        mPhone.addTextChangedListener(this);
        mBtnSend.setOnClickListener(this);
        mUserAddress.addTextChangedListener(this);
        mUserAge.addTextChangedListener(this);
        mUserAge.setOnClickListener(this);
    }

    private void initData() {
        if (DoctorApplication.mUser != null && !StringUtils.isEmpty(DoctorApplication.mUser.getUserName())) {
            mPhone.setText(DoctorApplication.mUser.getUserName());
        }

        if (DoctorMainActivity.mUserInfo != null && !StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getNickName())) {
            mTrueName.setText(DoctorMainActivity.mUserInfo.getNickName());
        }

        if (DoctorMainActivity.mUserInfo != null && !StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getProvince())) {
            mUserAddress.setText(DoctorMainActivity.mUserInfo.getProvince());
        }

        if (DoctorMainActivity.mUserInfo != null && !StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getCity())) {
            String province = mUserAddress.getText().toString().trim();
            mUserAddress.setText(province + "、" + DoctorMainActivity.mUserInfo.getCity());
        }

        if (DoctorMainActivity.mUserInfo != null && !StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getArea())) {
            String province = mUserAddress.getText().toString().trim();
            mUserAddress.setText(province + "、" + DoctorMainActivity.mUserInfo.getArea());
        }

        if (DoctorMainActivity.mUserInfo != null && !StringUtils.isEmpty(DoctorMainActivity.mUserInfo.getBrithday())) {
            mUserAge.setText(DoctorMainActivity.mUserInfo.getBrithday());
        }

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_doctor_order;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        buttonEnable();
    }


    private void buttonEnable() {
        String orderInfo = mOrderInfo.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String userName = mTrueName.getText().toString().trim();
        String userAge = mUserAge.getText().toString().trim();
        String userAddress = mUserAddress.getText().toString().trim();


        if (mGrade.contains("生活") || mGrade.contains("医疗") || mGrade.contains("学校")) {
            if (!StringUtils.isEmpty(orderInfo) && StringUtils.isTelActive(phone) && !StringUtils.isEmpty(userName)) {
                mBtnSend.setEnabled(true);
            } else {
                mBtnSend.setEnabled(false);
            }
        } else {
            if (!StringUtils.isEmpty(orderInfo) && StringUtils.isTelActive(phone) && !StringUtils.isEmpty(userName)
                    && !StringUtils.isEmpty(userAge) && !StringUtils.isEmpty(userAddress)) {
                mBtnSend.setEnabled(true);
            } else {
                mBtnSend.setEnabled(false);
            }
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == mBtnSend.getId()) {
            sendOrderInfo();
        } else if (id == mUserAge.getId()) {
            showSelectBirthday();
        }
    }

    public static final String DATEPICKER_TAG = "datepicker";

    private void showSelectBirthday() {
        final Calendar calendar = Calendar.getInstance();

        String birthday = mUserAge.getText().toString().trim();

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
                mUserAge.setText(birthday);
                buttonEnable();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
        datePickerDialog.setVibrate(true);
        datePickerDialog.setYearRange(1930, 2028);
        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);

    }


    private void sendOrderInfo() {
        String orderInfo = mOrderInfo.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        String userName = mTrueName.getText().toString().trim();
        String userAge = mUserAge.getText().toString().trim();
        String userAddress = mUserAddress.getText().toString().trim();
        String sexStr = "";
        if (DoctorMainActivity.mUserInfo != null && DoctorMainActivity.mUserInfo.getUserSex() != 0) {
            int sex = DoctorMainActivity.mUserInfo.getUserSex();
            if (sex == 2) {
                sexStr = "女";
            } else {
                sexStr = "男";
            }
        } else {
            sexStr = "男";
        }

        showDialog();
        OrderMsg msg = new OrderMsg();
        msg.setUserId(DoctorApplication.mUser.getUserId());
        msg.setUserName(userName);
        msg.setUserAge(userAge);
        msg.setUserAddress(userAddress);
        msg.setDoctorName(mDoctorName);
        msg.setDoctorId(mDoctorId);
        msg.setDoctorPhone(mPhoneNumber);
        msg.setOrderInfo(orderInfo);
        msg.setPhoneNumber(phone);
        msg.setSex(sexStr);

        HttpUtils.postDataFromServer(HttpAddress.SEND_DOCTOR_ORDER, JSONObject.toJSONString(msg), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                showToast(message);
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                showTipDialog();
            }
        });

    }

    private void showTipDialog() {
        new MaterialDialog.Builder(this)
                .title("温馨提示")
                .content("您的信息已发送至医生手机，请留心关注App中的消息回复")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .positiveText("确定")
                .show();

    }
}
