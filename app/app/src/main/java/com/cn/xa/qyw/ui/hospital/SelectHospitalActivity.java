package com.cn.xa.qyw.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.HospitalAdapter;
import com.cn.xa.qyw.adapter.HospitalDoctorAdapter;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.city.CityTotal;
import com.cn.xa.qyw.entiy.HospitalDetailInfo;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.InputUtil;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 409160 on 2017/1/6.
 */
public class SelectHospitalActivity extends DoctorBaseActivity {

    private EditText search;
    private ImageView searchImage;
    private ListView mSearchResultList;
    private TextView mTvNoResult;
    private HospitalAdapter mSearchResultAdapter;
    private View mParent;
    private TextView mProvinceText;
    private TextView mCityText;
    private TextView mHospitalGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("选择所在医院");
        initView();
        initListener();
        initData();
    }

    private void initData() {
        showDialog();

        String province = mProvinceText.getText().toString().trim();
        String city = mCityText.getText().toString().trim();
        String grade = mHospitalGrade.getText().toString().trim();

        HospitalDetailInfo info = new HospitalDetailInfo();
        info.setHospital_city(city);
        info.setHospital_province(province);
        info.setHospital_grade(grade);

        HttpUtils.postDataFromServer(HttpAddress.QUERY_HOSPITAL_CITY, JSONObject.toJSONString(info), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();

                List<HospitalDetailInfo> list = JSONObject.parseArray(data, HospitalDetailInfo.class);

                mSearchResultAdapter = new HospitalAdapter(SelectHospitalActivity.this, false);
                mSearchResultList.setAdapter(mSearchResultAdapter);

                if (list == null || list.size() == 0) {
                    mSearchResultList.setVisibility(View.GONE);
                    mTvNoResult.setVisibility(View.VISIBLE);
                } else {
                    mSearchResultAdapter.setData(list);
                    mSearchResultList.setVisibility(View.VISIBLE);
                    mTvNoResult.setVisibility(View.GONE);
                }

            }
        });


    }

    private void initListener() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputUtil.closeInput(SelectHospitalActivity.this);
                    getData();
                    return true;
                }
                return false;
            }
        });

        mProvinceText.setOnClickListener(this);
        mCityText.setOnClickListener(this);
        mHospitalGrade.setOnClickListener(this);
        mSearchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HospitalDetailInfo detailInfo = mSearchResultAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("detailInfo", detailInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.hospital_province) {
            showProvince();
        } else if (id == R.id.hospital_city) {
            String province = mProvinceText.getText().toString().trim();

            if (!StringUtils.isEmpty(province)) {
                showSelectCity(province);
            } else {
                showProvince();
            }

        }else if(id == R.id.hospital_type){
            showHospitalType();
        }
    }

    private void showHospitalType() {
        new MaterialDialog.Builder(this)
                .title("选择类别")
                .items(R.array.hospital_type)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        String str = String.valueOf(text);
                        String currentGrade = mHospitalGrade.getText().toString().trim();
                        mHospitalGrade.setText(str);
                        if (str != null && !str.equals(currentGrade)) {
                            initData();
                        }
                    }
                })
                .show();
    }

    private void showProvince() {
        List<String> list = CityTotal.getProvince();
        new MaterialDialog.Builder(this)
                .title("选择省份")
                .items(list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String str = String.valueOf(text);
                        String province = mProvinceText.getText().toString().trim();
                        mProvinceText.setText(str);
                        if (str != null && !str.equals(province)) {
                            showSelectCity(str);
                        }

                    }
                })
                .show();
    }

    private void showSelectCity(String str) {
        List<String> list = CityTotal.getCity(str);
        new MaterialDialog.Builder(this)
                .title("选择市区")
                .items(list)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String str = String.valueOf(text);
                        String city = mCityText.getText().toString().trim();
                        mCityText.setText(str);
                        if (str != null && !str.equals(city)) {
                            initData();
                        }

                    }
                })
                .show();
    }

    private void initView() {
        mParent = findViewById(R.id.first_parent);
        search = (EditText) findViewById(R.id.search);
        searchImage = (ImageView) findViewById(R.id.search_img);
        mProvinceText = (TextView) findViewById(R.id.hospital_province);
        mHospitalGrade = (TextView) findViewById(R.id.hospital_type);
        mCityText = (TextView) findViewById(R.id.hospital_city);
        searchImage.setVisibility(View.VISIBLE);
        search.setHint("请输入要查询的医院关键字");

        mSearchResultList = (ListView) findViewById(R.id.search_result);
        mTvNoResult = (TextView) findViewById(R.id.tv_noresult);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search.getVisibility() == View.VISIBLE) {
                    getData();
                } else {
                    search.setVisibility(View.VISIBLE);
                    search.requestFocus();
                    InputUtil.openInput(SelectHospitalActivity.this, search);
                    mToolbarTitle.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.add_hospital).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getNewIntent(AddHospitalActivity.class);
                startActivityForResult(intent, 17);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 17) {
                initData();
            }
        }
    }

    private void getData() {
        String searchText = search.getText().toString().trim();
        if (StringUtils.isEmpty(searchText)) {
            showToast("请输入要搜的医院关键字");
        } else {
            searchHospital("%" + searchText + "%");
        }
    }

    private void searchHospital(String searchText) {
        showDialog();
        HttpUtils.postDataFromServer(HttpAddress.QUERY_HOSPITAL_NAME, searchText, new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();

                List<HospitalDetailInfo> list = JSONObject.parseArray(data, HospitalDetailInfo.class);

                mSearchResultAdapter = new HospitalAdapter(SelectHospitalActivity.this, true);
                mSearchResultList.setAdapter(mSearchResultAdapter);

                mParent.setVisibility(View.GONE);
                if (list == null || list.size() == 0) {
                    mSearchResultList.setVisibility(View.GONE);
                    mTvNoResult.setVisibility(View.VISIBLE);
                } else {
                    mSearchResultAdapter.setData(list);
                    mSearchResultList.setVisibility(View.VISIBLE);
                    mTvNoResult.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_select_hospital;
    }

    @Override
    public void onBackPressed() {

        if (search.getVisibility() == View.VISIBLE) {
            search.setVisibility(View.GONE);
            searchImage.setVisibility(View.VISIBLE);
            mToolbarTitle.setVisibility(View.VISIBLE);
            mParent.setVisibility(View.VISIBLE);
            return;
        }

        super.onBackPressed();
    }
}
