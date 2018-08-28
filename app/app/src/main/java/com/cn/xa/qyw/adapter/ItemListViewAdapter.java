package com.cn.xa.qyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.entiy.NewsDetail;
import com.cn.xa.qyw.ui.news.NewsDetailActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ItemListViewAdapter extends BaseAdapter implements View.OnClickListener{

    private final List<NewsDetail> mList;
    private final Context mContext;
    private  LayoutInflater mInflater;

    public ItemListViewAdapter(Context context, List<NewsDetail> list){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mInflater.inflate(R.layout.item_zixun_list_view,null);
            holder = new ViewHolder();
            holder.image1 = (SimpleDraweeView) view.findViewById(R.id.news_photo);
            holder.parent = view.findViewById(R.id.parent1);
            holder.title1 = (TextView) view.findViewById(R.id.title);
            holder.author1 = (TextView) view.findViewById(R.id.author1);
            holder.browseCount1 = (TextView) view.findViewById(R.id.browse_count_1);
            holder.parent.setOnClickListener(this);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        NewsDetail detail1 = mList.get(position);
        holder.parent.setTag(detail1);
        holder.title1.setText(detail1.getTitle());
        holder.image1.setImageURI(detail1.getNewsPhoto());
        holder.browseCount1.setText("浏览量：" + detail1.getBrowseCount() + "次");
        holder.author1.setText(detail1.getAuthor());
        return view;
    }

    @Override
    public void onClick(View v) {
        NewsDetail detail = (NewsDetail) v.getTag();
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("id", detail.getNewsId());
        intent.putExtra("url", detail.getContent());
        mContext.startActivity(intent);
    }

    class ViewHolder{
        private SimpleDraweeView image1;
        private TextView title1;
        private TextView author1;
        private TextView browseCount1;
        private View parent;
    }

}
