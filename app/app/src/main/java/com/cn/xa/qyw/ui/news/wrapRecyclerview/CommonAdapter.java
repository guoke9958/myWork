package com.cn.xa.qyw.ui.news.wrapRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.xa.qyw.ui.news.wrapRecyclerview.base.ViewHolder;

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
        View view = mInflater.inflate(mLayoutId,parent ,false);
        ViewHolder holder = new ViewHolder(mContext,view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        convert(holder, mDatas.get(position),position);
        holder.setOnClickListener(holder.getConvertView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewListener != null){
                    onRecyclerViewListener.onItemClick(holder.getConvertView(),position);
                }
            }
        });
        holder.setOnLongClickListener(holder.getConvertView(), new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null){
                    onRecyclerViewListener.onItemLongClick(position);
                }
                return false;
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

}
