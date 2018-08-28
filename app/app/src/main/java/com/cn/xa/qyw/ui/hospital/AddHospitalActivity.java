package com.cn.xa.qyw.ui.hospital;

import android.content.DialogInterface;
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
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.city.CityTotal;
import com.cn.xa.qyw.entiy.HospitalDetailInfo;
import com.cn.xa.qyw.entiy.UpdateHospitalName;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by 409160 on 2017/1/9.
 */
public class AddHospitalActivity extends DoctorBaseActivity {

    private EditText mEditHospitalName;
    private EditText mSelectHospitalGrade,mSelectHospitalProvince,mSelectHospitalCity;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("添加医院");
        initView();
        initListener();
    }

    private void initView() {
        mEditHospitalName = (EditText)findViewById(R.id.hospital_name);
        mSelectHospitalGrade = (EditText)findViewById(R.id.hospital_grade);
        mSelectHospitalProvince = (EditText)findViewById(R.id.hospital_province);
        mSelectHospitalCity = (EditText)findViewById(R.id.hospital_city);
        mBtn = (Button)findViewById(R.id.send_btn);
    }

    private void initListener() {
        mEditHospitalName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initButton();
            }
        });
        mSelectHospitalGrade.setOnClickListener(this);
        mSelectHospitalProvince.setOnClickListener(this);
        mSelectHospitalCity.setOnClickListener(this);
        mBtn.setOnClickListener(this);
    }

    private void initButton(){
        String hospitalName = mEditHospitalName.getText().toString().trim();
        String hospitalProvince = mSelectHospitalProvince.getText().toString().trim();
        String hospitalGrade = mSelectHospitalGrade.getText().toString().trim();
        String hospitalCity = mSelectHospitalCity.getText().toString().trim();

        if(!StringUtils.isEmpty(hospitalName)&&!StringUtils.isEmpty(hospitalProvince)&&
                !StringUtils.isEmpty(hospitalGrade)&&!StringUtils.isEmpty(hospitalCity)){
            mBtn.setEnabled(true);
        }else{
            mBtn.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.hospital_grade){
            showSelectHospitalGrade();
        }else if(id == R.id.hospital_province){
            showSelectHospitalProvince();
        }else if(id == R.id.hospital_city){
            String province = mSelectHospitalProvince.getText().toString().trim();

            if(!StringUtils.isEmpty(province)){
                showSelectHospitalCity(province);
            }else{
                showSelectHospitalProvince();
            }
        }else if(id == R.id.send_btn){
            addHospital();
        }
    }

    private void addHospital() {
        String hospitalName = mEditHospitalName.getText().toString().trim();
        String hospitalProvince = mSelectHospitalProvince.getText().toString().trim();
        String hospitalGrade = mSelectHospitalGrade.getText().toString().trim();
        String hospitalCity = mSelectHospitalCity.getText().toString().trim();

        HospitalDetailInfo info = new HospitalDetailInfo();
        info.setHospital_city(hospitalCity);
        info.setHospital_province(hospitalProvince);
        info.setHospital_grade(hospitalGrade);
        info.setIs_pass(0);
        info.setHospital_name(hospitalName);

        showDialog();
        HttpUtils.postDataFromServer(HttpAddress.ADD_HOSPITAL, JSONObject.toJSONString(info), new NetworkResponseHandler() {
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
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("温馨提示")
                .content("您已成功提交医院信息，管理人员正在审核")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .positiveText("我知道了")
                .show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void showSelectHospitalCity(String province) {
        List<String> list = CityTotal.getCity(province);
        new MaterialDialog.Builder(this)
                .title("选择市区")
                .items(list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String str = String.valueOf(text);
                        String city = mSelectHospitalCity.getText().toString().trim();
                        mSelectHospitalCity.setText(str);
                        initButton();
                    }
                })
                .show();
    }

    private void showSelectHospitalProvince() {
        List<String> list = CityTotal.getProvince();
        new MaterialDialog.Builder(this)
                .title("选择省份")
                .items(list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String str = String.valueOf(text);
                        String province = mSelectHospitalProvince.getText().toString().trim();
                        mSelectHospitalProvince.setText(str);
                        if (str != null && !str.equals(province)) {
                            showSelectHospitalCity(str);
                        }

                        initButton();

                    }
                })
                .show();
    }

    private void showSelectHospitalGrade() {
        new MaterialDialog.Builder(this)
                .title("选择医院等级")
                .items(R.array.hospital_grade)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String str = String.valueOf(text);
                        mSelectHospitalGrade.setText(str);
                        initButton();

                    }
                })
                .show();
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_add_hospital;
    }
}
