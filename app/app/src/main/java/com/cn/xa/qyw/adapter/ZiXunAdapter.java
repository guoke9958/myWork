package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.NewsDetail;
import com.cn.xa.qyw.ui.discover.DiscoverNewsActivity;
import com.cn.xa.qyw.ui.news.NewsDetailActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ZiXunAdapter extends BaseAdapter implements View.OnClickListener{

    private final Context mContext;
    private final LayoutInflater mInflater;
    private HashMap<String, List<NewsDetail>> map = new HashMap<>();

    public ZiXunAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setListNews(String position, List<NewsDetail> list) {
        map.put(position, list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return map.size();
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
            view = mInflater.inflate(R.layout.item_zixun_listview, null);
            holder.zixun_title_parent = view.findViewById(R.id.zixun_title_parent);
            holder.title = (TextView) view.findViewById(R.id.zixun_title);

            holder.listView = (ListView)view.findViewById(R.id.zixun_item_listview);
            holder.zixun_title_parent.setOnClickListener(this);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        if (position == 0) {
            holder.title.setText("医疗新闻");
        } else if (position == 1) {
            holder.title.setText("医生文章");
        } else if (position == 2) {
            holder.title.setText("急救常识");
        } else if (position == 3) {
            holder.title.setText("疑难杂症");
        }

        holder.zixun_title_parent.setTag(position);

        List<NewsDetail> list = map.get(String.valueOf(position));

        ItemListViewAdapter adapter = new ItemListViewAdapter(mContext, list);
        holder.listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.parent1 || id == R.id.parent2 || id == R.id.parent3) {
            NewsDetail detail = (NewsDetail) v.getTag();
            Intent intent = new Intent(mContext, NewsDetailActivity.class);
            intent.putExtra("id", detail.getNewsId());
            intent.putExtra("url", detail.getContent());
            mContext.startActivity(intent);
        } else if (id == R.id.zixun_title_parent) {
            int position = (int) v.getTag();
            Intent intent = new Intent();
            if (position == 0) {
                intent.putExtra("title","医疗新闻");
                intent.setClass(mContext, DiscoverNewsActivity.class);
            }

            mContext.startActivity(intent);
        }


    }

    class ViewHolder {

        private TextView title;

        private View zixun_title_parent;
        private ListView  listView;

    }

}
