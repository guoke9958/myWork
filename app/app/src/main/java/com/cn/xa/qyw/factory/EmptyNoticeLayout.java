package com.cn.xa.qyw.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cn.xa.qyw.R;

public class EmptyNoticeLayout extends LinearLayout {


    private View layoutView;
    private View baseLoadEmpty;
    private View baseLoadError;

    //生成当前类的构造方法（以下三种都是）
    public EmptyNoticeLayout(Context context) {
        //        super(context);
        this(context, null); //调用两个参数的构造
    }

    public EmptyNoticeLayout(Context context, AttributeSet attrs) {
        //        super(context, attrs);
        this(context, attrs, 0);//调用三个参数的构造，在三个参数的构造中实现具体逻辑
    }

    public EmptyNoticeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutView = LayoutInflater.from(context).inflate(R.layout.base_layout_data_view, this);
        layoutView.setVisibility(GONE);
        baseLoadError = layoutView.findViewById(R.id.base_load_error);
        baseLoadEmpty = layoutView.findViewById(R.id.base_load_empty);

    }

    public void showErrorView(View view){
        view.setVisibility(GONE);
        layoutView.setVisibility(VISIBLE);
        baseLoadEmpty.setVisibility(GONE);
        baseLoadError.setVisibility(VISIBLE);
        baseLoadError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEmpty != null){
                    clickEmpty.onClickListener(v);
                }
            }
        });
    }

    public void showEmptyView(View view){
        view.setVisibility(GONE);
        layoutView.setVisibility(VISIBLE);
        baseLoadEmpty.setVisibility(VISIBLE);
        baseLoadError.setVisibility(GONE);
    }

    private ClickEmpty clickEmpty;
    public void setBaseLoadEmpty(ClickEmpty clickEmpty){
        this.clickEmpty = clickEmpty;
    }
    public interface ClickEmpty{
        void onClickListener(View v);
    }
}
