package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.News;
import com.cn.xa.qyw.ui.news.NewsDetailActivity;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 409160 on 2016/7/30.
 */
public class DiscoverNewsAdapter extends BaseAdapter implements View.OnClickListener {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final ArrayList<News> mList;

    public DiscoverNewsAdapter(Context context){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mList = new ArrayList<News>();
    }

    @Override
    public int getCount() {
        return mList.size();
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
        if(view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_discover_news_listview,null);
            holder.newImage0 = (SimpleDraweeView)view.findViewById(R.id.news_img_0);
            holder.newImage1 = (SimpleDraweeView)view.findViewById(R.id.news_img_1);
            holder.newImage2 = (SimpleDraweeView)view.findViewById(R.id.news_img_2);
            holder.createTime = (TextView)view.findViewById(R.id.create_time);
            holder.title1 = (TextView)view.findViewById(R.id.title1);
            holder.title2 = (TextView)view.findViewById(R.id.title2);
            holder.title3 = (TextView)view.findViewById(R.id.title3);

            holder.parent1 = view.findViewById(R.id.parent1);
            holder.parent2 = view.findViewById(R.id.parent2);
            holder.parent3 = view.findViewById(R.id.parent3);


            holder.parent1.setOnClickListener(this);
            holder.parent2.setOnClickListener(this);
            holder.parent3.setOnClickListener(this);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        News news = mList.get(position);

        holder.createTime.setText(DateUtils.getTimeData(news.getCreateTime()));

        try {
            JSONObject json = new JSONObject(news.getNewsFirst());
            holder.title1.setText(json.optString("title"));
            holder.newImage0.setImageURI(json.optString("url"));
            holder.parent1.setTag(json.optInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(StringUtils.isEmpty(news.getNewsSecond())){
                holder.parent2.setVisibility(View.GONE);
            }else{
                holder.parent2.setVisibility(View.VISIBLE);
                JSONObject json = new JSONObject(news.getNewsSecond());
                holder.title2.setText(json.optString("title"));
                holder.newImage1.setImageURI(json.optString("url"));
                holder.parent2.setTag(json.optInt("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {

            if(StringUtils.isEmpty(news.getNewsThree())){
                holder.parent3.setVisibility(View.GONE);
            }else{
                holder.parent3.setVisibility(View.VISIBLE);
                JSONObject json = new JSONObject(news.getNewsThree());
                holder.title3.setText(json.optString("title"));
                holder.newImage2.setImageURI(json.optString("url"));
                holder.parent3.setTag(json.optInt("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setData(List<News> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = (int) v.getTag();

        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("id", id);
        mContext.startActivity(intent);
    }

    class ViewHolder{
        SimpleDraweeView newImage0;
        SimpleDraweeView newImage1;
        SimpleDraweeView newImage2;

        TextView createTime;
        TextView title1;
        TextView title2;
        TextView title3;

        View parent1;
        View parent2;
        View parent3;

    }
}
