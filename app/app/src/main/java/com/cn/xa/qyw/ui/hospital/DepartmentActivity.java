package com.cn.xa.qyw.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.DepartmentDoctorAdapter;
import com.cn.xa.qyw.adapter.HospitalDoctorAdapter;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.datasource.DepartmentAsyncDataSource;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.ui.doctor.DoctorDetailActivity;
import com.cn.xa.qyw.utils.InputUtil;
import com.cn.xa.qyw.utils.Lg;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCNormalHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 409160 on 2016/7/7.
 */
public class DepartmentActivity extends DoctorBaseActivity {

    private int mDepartmentId;
    private GridView mListView;
    private MVCNormalHelper<List<SimpleDoctor>> mvcHelper;
    private DepartmentDoctorAdapter mAdapter;
    private ScrollView mScrollView;
    private EditText search;
    private List<SimpleDoctor> mData;
    private ImageView searchImage;
    private ListView mSearchResultList;
    private TextView mTvNoResult;
    private HospitalDoctorAdapter mSerchResultAdapter;
    private String mGrade;
    private String mGradeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String departmentName = getIntent().getStringExtra("department_name");
        mGrade = getIntent().getStringExtra("grade");
        mGradeId = getIntent().getStringExtra("grade_id");
        mDepartmentId = getIntent().getIntExtra("department_id", 0);
        mToolbarTitle.setText(departmentName);
        initView();
        initData();
        initListener();

        if("生活服务".equals(mGrade)||"学校".equals(mGrade)||"APP客服".equals(mGrade)){
            searchImage.setVisibility(View.GONE);
        }else{
            searchImage.setVisibility(View.VISIBLE);
        }

    }

    private void initListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString() == null || "".equals(s.toString())) {
                    mScrollView.setVisibility(View.VISIBLE);
                    mSearchResultList.setVisibility(View.GONE);
                    mTvNoResult.setVisibility(View.GONE);
                } else {

                    mScrollView.setVisibility(View.GONE);
                    List<SimpleDoctor> list = getResultList(s.toString());

                    HashMap<String, List<SimpleDoctor>> map = getResultMap(list);

                    Lg.e("map.size=======" + map.size());

                    mSerchResultAdapter = new HospitalDoctorAdapter(DepartmentActivity.this, map);
                    mSearchResultList.setAdapter(mSerchResultAdapter);

                    if (map.size() == 0) {
                        mSearchResultList.setVisibility(View.GONE);
                        mTvNoResult.setVisibility(View.VISIBLE);
                    } else {
                        mSearchResultList.setVisibility(View.VISIBLE);
                        mTvNoResult.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!"13".equals(mGradeId) || !"通惠商城".equals(mGrade)){
                    Intent intent = getNewIntent(DoctorDetailActivity.class);
                    SimpleDoctor item = mAdapter.getItem(position);
                    intent.putExtra("doctor", item);
                    intent.putExtra("grade", mGrade);
                    intent.putExtra("grade_id",mGradeId);
                    startActivity(intent);
                }

            }
        });

    }

    private HashMap<String, List<SimpleDoctor>> getResultMap(List<SimpleDoctor> list) {
        HashMap<String, List<SimpleDoctor>> map = new HashMap<>();

        for (SimpleDoctor doctor : list) {
            List<SimpleDoctor> doctors = map.get(doctor.getHospitalName());
            if (doctors == null) {
                List<SimpleDoctor> localList = new ArrayList<>();
                localList.add(doctor);
                map.put(doctor.getHospitalName(), localList);
            } else {
                doctors.add(doctor);
            }
        }

        return map;
    }

    private List<SimpleDoctor> getResultList(String s) {

        List<SimpleDoctor> list = new ArrayList<>();

        if (mData == null) {
            return list;
        }

        for (SimpleDoctor doctor : mData) {
            if (doctor.getDoctorName().contains(s) || doctor.getHospitalName().contains(s)) {
                list.add(doctor);
            }
        }

        return list;
    }

    private void initData() {
        mvcHelper = new MVCNormalHelper<List<SimpleDoctor>>(mScrollView);
        mvcHelper.setDataSource(new DepartmentAsyncDataSource(mDepartmentId, "西安", mGrade,mGradeId));
        mvcHelper.setAdapter(new DepartAdapter());
        mvcHelper.refresh();
    }


    class DepartAdapter implements IDataAdapter<List<SimpleDoctor>> {

        @Override
        public void notifyDataChanged(List<SimpleDoctor> addDepartmentses, boolean isRefresh) {
            mData = addDepartmentses;
            mAdapter = new DepartmentDoctorAdapter(DepartmentActivity.this,mGrade);
            mAdapter.setData(addDepartmentses);
            mListView.setAdapter(mAdapter);
        }

        @Override
        public List<SimpleDoctor> getData() {
            return mData;
        }

        @Override
        public boolean isEmpty() {
            if (mData == null || mData.size() == 0) {
                return true;
            } else {
                return false;
            }

        }
    }


    private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mListView = (GridView) findViewById(R.id.depart_doctor_list_view);
        search = (EditText) findViewById(R.id.search);
        searchImage = (ImageView) findViewById(R.id.search_img);
        searchImage.setVisibility(View.VISIBLE);

        mSearchResultList = (ListView) findViewById(R.id.search_result);
        mTvNoResult = (TextView) findViewById(R.id.tv_noresult);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setVisibility(View.VISIBLE);
                search.requestFocus();
                InputUtil.openInput(DepartmentActivity.this, search);
                searchImage.setVisibility(View.GONE);
                mToolbarTitle.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_department_detail;
    }

    @Override
    public void onBackPressed() {

        if (search.getVisibility() == View.VISIBLE) {
            search.setVisibility(View.GONE);
            searchImage.setVisibility(View.VISIBLE);
            mToolbarTitle.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.VISIBLE);
            mSearchResultList.setVisibility(View.GONE);
            mScrollView.fullScroll(ScrollView.FOCUS_UP);
            return;
        }

        super.onBackPressed();
    }
}
