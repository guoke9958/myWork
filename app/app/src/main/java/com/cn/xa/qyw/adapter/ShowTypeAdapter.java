package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.ShopType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 409160 on 2016/7/29.
 */
public class ShowTypeAdapter extends BaseAdapter {

    private final ArrayList<ShopType> list;
    private final Context mContext;
    private  LayoutInflater mInflater;

    public ShowTypeAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        list = new ArrayList<ShopType>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ShopType getItem(int position) {
        return list.get(position);
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
            view = mInflater.inflate(R.layout.item_showtype_gridview, null);
            holder.tvName = (TextView) view.findViewById(R.id.department_name_tv);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(list.get(position).getName());
        return view;
    }

    public void setData(List<ShopType> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvName;
    }
}
