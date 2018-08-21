package com.cn.xa.qyw.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.entiy.VideoType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/3.
 */
public class VideoTypeAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final List<VideoType> mList;
    private final Activity mActivity;

    public VideoTypeAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mList = new ArrayList<VideoType>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public VideoType getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  mList.get(position).getId();
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
        holder.tvName.setText(getItem(position).getTypeName());

        return view;
    }

    public void setData(List<VideoType> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvName;
    }
}
