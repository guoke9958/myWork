package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
public class GGAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mList = new ArrayList<>();

    public GGAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        mList.add("求一位能做Android开发的工作人员");
        mList.add("想购买一个奥迪车，不知道有出售的没");
        mList.add("养个泰迪犬，希望爱狗人士可以介绍");
        mList.add("西安手机分期购买地址?大学生分期付款办理7可以零首付吗");
        mList.add("华为荣耀v8高配版运存4g内存64 在保全套");
        mList.add("厂家直销散热器泡沫铜耐高温超薄金属铜 量大从优");
        mList.add("C65型自复式过欠压保护器 厂家直供");
        mList.add("学校室内游泳馆向外承包");
        mList.add("西安私人影院加盟聚空间影咖");
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
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

        if(view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_gg_listview, null);
            holder.contextTv = (TextView) view.findViewById(R.id.item_name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.contextTv.setText(mList.get(position%mList.size()));
        return view;
    }

    class ViewHolder {
        private TextView contextTv;
    }

}
