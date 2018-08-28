package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.ShowSearchResult;
import com.cn.xa.qyw.entiy.SimpleDoctor;
import com.cn.xa.qyw.ui.doctor.DoctorDetailActivity;
import com.cn.xa.qyw.ui.hospital.DepartmentDetailActivity;
import com.cn.xa.qyw.ui.hospital.HospitalDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/7/25.
 */
public class HospitalDoctorAdapter extends BaseAdapter implements View.OnClickListener {

    private final Context mContext;
    private final HashMap<String, List<SimpleDoctor>> mMap;
    private final LayoutInflater mInflater;
    private List<ShowSearchResult> mList = new ArrayList<>();
    private DepartmentDoctorAdapter mAdapter;


    public HospitalDoctorAdapter(Context context, HashMap<String, List<SimpleDoctor>> map) {
        this.mContext = context;
        this.mMap = map;
        this.mInflater = LayoutInflater.from(context);
        mList.clear();

        Set<String> list = map.keySet();

        for (String hospitalName : list) {
            ShowSearchResult result = new ShowSearchResult();
            result.setHospitalName(hospitalName);
            List<SimpleDoctor> doctors = map.get(hospitalName);

            if (doctors.size() > 4) {
                result.setShowAll(2);
            } else {
                result.setShowAll(1);
            }

            result.setList(doctors);

            mList.add(result);
        }

    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_hospital_doctor_result_view, null);
            holder.hospitalName = (TextView) view.findViewById(R.id.hospital_name);
            holder.doctorsGrid = (GridView) view.findViewById(R.id.doctor_grid_view);
            holder.seeMore = (TextView) view.findViewById(R.id.click_see_more);
            holder.departmentDetail = (ImageView)view.findViewById(R.id.department_detail);
            holder.hospitalDetail = (ImageView)view.findViewById(R.id.hospital_detail);
            holder.seeMore.setOnClickListener(this);
            holder.departmentDetail.setOnClickListener(this);
            holder.hospitalDetail.setOnClickListener(this);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ShowSearchResult result = mList.get(position);
        holder.hospitalName.setText(result.getHospitalName());
        holder.seeMore.setTag(position);
        holder.departmentDetail.setTag(position);
        holder.hospitalDetail.setTag(position);

        mAdapter = new DepartmentDoctorAdapter(mContext,"");
        holder.doctorsGrid.setAdapter(mAdapter);

        if (result.isShowAll() == 1) {
            holder.seeMore.setVisibility(View.GONE);
            mAdapter.setData(result.getList());
        } else if (result.isShowAll() == 2) {
            holder.seeMore.setVisibility(View.VISIBLE);

            List<SimpleDoctor> list = new ArrayList<>();
            for(int i = 0;i<4;i++){
                list.add(result.getList().get(i));
            }
            mAdapter.setData(list);

            holder.seeMore.setText("点击查看更多");
        } else if (result.isShowAll() == 3) {
            holder.seeMore.setVisibility(View.VISIBLE);
            holder.seeMore.setText("点击收起列表");
            mAdapter.setData(result.getList());
        }

        holder.doctorsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,DoctorDetailActivity.class);
                SimpleDoctor item = mAdapter.getItem(position);
                intent.putExtra("doctor",item);
                mContext.startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int position = (int) v.getTag();
        ShowSearchResult result = mList.get(position);

        Intent intent = new Intent();
        if(id==R.id.department_detail){
            intent.setClass(mContext,DepartmentDetailActivity.class);
            mContext.startActivity(intent);
        }else if(id==R.id.hospital_detail){
            intent.setClass(mContext, HospitalDetailActivity.class);
            intent.putExtra("hospital_name", result.getHospitalName());
            mContext.startActivity(intent);
        }else{
            result.setShowAll(result.isShowAll() == 2 ? 3 : 2);
            notifyDataSetInvalidated();
        }
    }

    class ViewHolder {
        TextView hospitalName;
        GridView doctorsGrid;
        TextView seeMore;
        ImageView departmentDetail;
        ImageView hospitalDetail;
    }

}
