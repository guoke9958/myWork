package com.cn.xa.qyw.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.DepartmentAdapter;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.datasource.HospitalAsyncDataSource;
import com.cn.xa.qyw.entiy.AddDepartments;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.StringUtils;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCNormalHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
public class DepartmentAllActivity extends DoctorBaseActivity {

    private GridView mListView;
    private DepartmentAdapter mAdapter;
    private ScrollView mScrollView;
    private List<AddDepartments> mData = new ArrayList<>();
    private String mGrade;
    private String mGradeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("全部科室");
        mGrade = getIntent().getStringExtra("grade");
        if(!StringUtils.isEmpty(mGrade)){
            mToolbarTitle.setText(mGrade);
        }
        mGradeId = getIntent().getStringExtra("grade_id");
        mData = JSONObject.parseArray(getIntent().getStringExtra("department_all"),AddDepartments.class);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AddDepartments department = mAdapter.getItem(position);
                Intent intent = getNewIntent(DepartmentActivity.class);
                intent.putExtra("department_id",department.getId());
                intent.putExtra("department_name",department.getDepartmentsName());
                intent.putExtra("grade",department.getDepartmentsName());
                intent.putExtra("grade_id",department.getId());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mAdapter =  new DepartmentAdapter(this);
        mListView.setAdapter(mAdapter);

        if(mData!=null&&mData.size()>0){
            mAdapter.setData(mData);
        }

        if(!StringUtils.isEmpty(mGrade)){
            showDialog();
            HttpUtils.postDataFromServer(HttpAddress.GET_DEPARTMENT_GRADE,"%"+mGradeId+"%", new NetworkResponseHandler() {
                @Override
                public void onFail(String message) {
                    dismissDialog();
                    showToast("获取失败");
                }

                @Override
                public void onSuccess(String data) {
                    dismissDialog();
                    if(!StringUtils.isEmpty(data)){
                        List<AddDepartments> list = JSONObject.parseArray(data, AddDepartments.class);
                        mAdapter.setData(list);
                    }else{
                        showToast("获取列表为空");
                    }

                }
            });
        }


    }

    private void initView() {
        mScrollView = (ScrollView)findViewById(R.id.home_scrollview);
        mListView = (GridView)findViewById(R.id.department_gridview);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_all_department;
    }
}
