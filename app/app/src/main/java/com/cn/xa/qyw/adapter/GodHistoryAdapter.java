package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.CapitalHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/8/10.
 */
public class GodHistoryAdapter extends BaseAdapter {

    private int mType;
    private LayoutInflater mInflater;
    private List<CapitalHistory> mList = new ArrayList<>();

    public GodHistoryAdapter(Context context, int type) {
        this.mInflater = LayoutInflater.from(context);
        mType = type;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void setData(List<CapitalHistory> list) {
        mList.clear();
        appendData(list);
    }

    public void appendData(List<CapitalHistory> list) {
        mList.addAll(list);
        notifyDataSetChanged();

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
            view = mInflater.inflate(R.layout.item_god_history_view, null);
            holder.pay_type = (TextView) view.findViewById(R.id.pay_type);
            holder.god_type = (TextView) view.findViewById(R.id.god_type);
            holder.price = (TextView) view.findViewById(R.id.price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        CapitalHistory his = mList.get(position);

        if (his.getCapitalType() == 1) {
            holder.god_type.setText("充值");

            if ("ali".equals(his.getPayType())) {
                holder.pay_type.setText(" 支付宝 ");
            } else if ("wx".equals(his.getPayType())) {
                holder.pay_type.setText(" 微信 ");
            }

        } else if (his.getCapitalType() == 5) {
            holder.pay_type.setText("赠送礼包");
            holder.god_type.setText(" 获取 ");
        } else if (his.getCapitalType() == 2) {

            if (mType == 6) {
                holder.pay_type.setText(" 帮助患者 ");

                String name = his.getUserName();
                    holder.god_type.setText(name + "解决疾病问题获得");

            } else {

                holder.pay_type.setText(" 打赏 ");

                String name = his.getToUserName();
                if (his.getToUserType() == 1) {
                    name += "医生";
                }

                holder.god_type.setText("向" + name + "赠送");
            }

        }


        holder.price.setText(" " + his.getChange() + " ");


        return view;
    }

    class ViewHolder {
        TextView pay_type;
        TextView god_type;
        TextView price;
    }

}
