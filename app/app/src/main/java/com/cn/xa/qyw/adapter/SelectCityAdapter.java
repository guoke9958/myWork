package com.cn.xa.qyw.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.item.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 409160 on 2016/7/7.
 */
public class SelectCityAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<String> mList = new ArrayList<>();

    public SelectCityAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity);
    }

    public void initData(List<String> list, int type, boolean isShow) {
        mList.clear();

        if (type != 0 && isShow) {
            mList.add(".../上一级");
        }
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
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
            view = mInflater.inflate(R.layout.item_city_listview, null);
            holder.itemName = (TextView) view.findViewById(R.id.item_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.itemName.setText(getItem(position));

        return view;
    }

    class ViewHolder {
        TextView itemName;
    }

}
