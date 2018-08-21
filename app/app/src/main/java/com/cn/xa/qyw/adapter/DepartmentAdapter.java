package com.cn.xa.qyw.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.AddDepartments;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/3.
 */
public class DepartmentAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final List<AddDepartments> mList;
    private final Activity mActivity;
    private List<AddDepartments> mData;

    public DepartmentAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mList = new ArrayList<AddDepartments>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AddDepartments getItem(int position) {
        return mList.get(position);
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
            view = mInflater.inflate(R.layout.item_department_gridview, null);
            holder.tvName = (TextView) view.findViewById(R.id.department_name_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(getItem(position).getDepartmentsName());

        if (getItem(position).getId() == -1) {
            holder.tvName.setTextColor(mActivity.getResources().getColor(R.color.color_bg_green));
        } else {
            holder.tvName.setTextColor(mActivity.getResources().getColor(R.color.black));
        }

        return view;
    }

    public void setData(List<AddDepartments> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvName;
    }
}
