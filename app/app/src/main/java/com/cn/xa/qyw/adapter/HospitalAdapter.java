package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.HospitalDetailInfo;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public class HospitalAdapter extends BaseAdapter {

    private final boolean mIsShow;
    private LayoutInflater mInflater;
    private List<HospitalDetailInfo> mList = new ArrayList<>();

    public HospitalAdapter(Context context,boolean isShow) {
        mInflater = LayoutInflater.from(context);
        mIsShow = isShow;

    }

    public void setData(List<HospitalDetailInfo> list) {
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public HospitalDetailInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_select_hospital_listview, null);
            holder.tv = (TextView) view.findViewById(R.id.item_name);
            holder.info = (TextView) view.findViewById(R.id.item_info);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        HospitalDetailInfo item = getItem(position);
        holder.tv.setText(item.getHospital_name());

        String info = item.getHospital_province()+" | "+item.getHospital_city();
        String grade = item.getHospital_grade();

        if(!StringUtils.isEmpty(grade)){
            info = info + " | "+grade;
        }

        if(!mIsShow){
            holder.info.setVisibility(View.GONE);
        }else{
            holder.info.setVisibility(View.VISIBLE);
            holder.info.setText(info);
        }


        return view;
    }

    class ViewHolder {
        TextView tv;
        TextView info;
    }
}
