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

/**
 * Created by 409160 on 2016/7/7.
 */
public class AboutAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final ArrayList<MenuItem> mList;

    public AboutAdapter(Activity activity){
        mInflater = LayoutInflater.from(activity);
        mList = new ArrayList<MenuItem>();
        initData();
    }

    private void initData() {
        mList.add(new MenuItem(MenuItem.TYPE_DISCOVER_NEWS,"功能介绍", R.mipmap.sideslip_icon_feedback));
        mList.add(new MenuItem(MenuItem.TYPE_DISCOVER_DOCTOR_ARTICLE,"系统通知", R.mipmap.sideslip_icon_feedback));
        mList.add(new MenuItem(MenuItem.TYPE_DISCOVER_FIRST_AID,"检查版本", R.mipmap.sideslip_icon_feedback));
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if(view==null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_discover_listview,null);
            holder.itemName = (TextView)view.findViewById(R.id.item_name);
            holder.itemIcon = (ImageView)view.findViewById(R.id.item_icon);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        MenuItem item = mList.get(position);
        holder.itemIcon.setVisibility(View.GONE);
        holder.itemName.setText(item.getName());
        holder.itemIcon.setImageResource(item.getIconResourceId());

        return view;
    }

    class ViewHolder{
        TextView itemName;
        ImageView itemIcon;
    }

}
