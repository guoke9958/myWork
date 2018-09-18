package com.cn.xa.qyw.ui.news.wrapRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.xa.qyw.ui.news.adapter.recyclerview.base.ItemViewDelegate;
import com.cn.xa.qyw.ui.news.adapter.recyclerview.base.ViewHolder;

import java.util.LinkedList;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends BaseRecyclerAdapter<T, ViewHolder>{
    protected Context mContext;
    protected int mLayoutId;
    protected LinkedList<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, LinkedList<T> datas){
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutId, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    protected abstract void convert(ViewHolder holder, T t, int position);


}
