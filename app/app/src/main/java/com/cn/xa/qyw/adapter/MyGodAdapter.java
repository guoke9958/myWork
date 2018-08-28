package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.MyGodItem;
import com.cn.xa.qyw.entiy.UserCapital;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/4.
 */
public class MyGodAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<MyGodItem> mList = new ArrayList<MyGodItem>();

    public MyGodAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        initData();
    }

    private void initData() {
        mList.add(new MyGodItem(MyGodItem.GOD_TYPE_YU_E, "余额", "0", R.mipmap.icon_god_balance));
        mList.add(new MyGodItem(MyGodItem.GOD_TYPE_SHOU_RU, "累计收入", "0", R.mipmap.icon_god_income));
        mList.add(new MyGodItem(MyGodItem.GOD_TYPE_XIAO_FEI, "累计消费", "0", R.mipmap.icon_god_consumption));
        mList.add(new MyGodItem(MyGodItem.GOD_TYPE_CHONG_ZHI, "累计充值", "0", R.mipmap.icon_god_top_up));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MyGodItem getItem(int position) {
        return mList.get(position);
    }

    public void setData(UserCapital capital) {
        for (MyGodItem item : mList) {
            int type = item.getType();
            if (MyGodItem.GOD_TYPE_YU_E == type) {
                item.setCount(((int) capital.getCapitalTotal() + capital.getIncomeCapital()) + "");
            } else if (MyGodItem.GOD_TYPE_CHONG_ZHI == type) {
                item.setCount(capital.getRechargeCapital() + "");
            } else if (MyGodItem.GOD_TYPE_SHOU_RU == type) {
                item.setCount(capital.getIncomeCapital() + "");
            } else if (MyGodItem.GOD_TYPE_XIAO_FEI == type) {
                item.setCount(capital.getExpandCapital() + "");
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = mInflater.inflate(R.layout.item_my_god_listview, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon_type);
        TextView tvShow = (TextView) view.findViewById(R.id.text_detail);
        TextView count = (TextView) view.findViewById(R.id.god_count);
        TextView detail = (TextView) view.findViewById(R.id.detail);

        MyGodItem item = mList.get(position);

        icon.setImageResource(item.getResourceId());
        tvShow.setText(item.getName());
        count.setText(item.getCount());
        if (item.getType() == MyGodItem.GOD_TYPE_YU_E) {
            detail.setVisibility(View.GONE);
        } else {
            detail.setVisibility(View.VISIBLE);
        }


        return view;
    }
}
