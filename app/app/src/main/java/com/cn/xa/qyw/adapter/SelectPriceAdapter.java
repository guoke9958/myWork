package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.SelectPriceItem;
import com.cn.xa.qyw.ui.slide.SelectPriceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class SelectPriceAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final SelectPriceActivity mContext;
    private List<SelectPriceItem> list = new ArrayList<>();

    public SelectPriceAdapter(SelectPriceActivity context){
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        list.add(new SelectPriceItem(R.mipmap.dq_pay_one,"5","手滑一下","5"));
        list.add(new SelectPriceItem(R.mipmap.dq_pay_two,"10","手抖一下","10"));
        list.add(new SelectPriceItem(R.mipmap.dq_pay_three,"50","开始爱了","50"));
        list.add(new SelectPriceItem(R.mipmap.dq_pay_four,"100","爱的深沉","100"));
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SelectPriceItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.layout_dq_info_item,null);

        ImageView item_pic = (ImageView)convertView.findViewById(R.id.item_pic);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView gift = (TextView)convertView.findViewById(R.id.gift);
        TextView price = (TextView)convertView.findViewById(R.id.price);

        price.setOnClickListener(mContext);
        price.setTag(position);

        SelectPriceItem item = list.get(position);

        item_pic.setImageResource(item.getResourceId());
        name.setText(item.getTitle() + "元");
        gift.setText(item.getTips());
        price.setText("¥ "+item.getPrice());

        return convertView;
    }
}
