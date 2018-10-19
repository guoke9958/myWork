package com.cn.xa.qyw.ui.news.wrapRecyclerview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cn.xa.qyw.R;
import com.handmark.pulltorefresh.library.LoadingLayoutBase;

/**
 * Created by zhouwk on 2015/12/30 0030.
 */
public class TmallFooterLayout extends LoadingLayoutBase {

    private FrameLayout mInnerLayout;
    private ImageView mCatImage;

    private AnimationDrawable animCat;

    public TmallFooterLayout(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.tmall_footer_loadinglayout, this);
        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        mCatImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_cat);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout.getLayoutParams();
        lp.gravity = Gravity.TOP;

        reset();
    }

    // 获取"加载头部"高度
    @Override
    public int getContentSize() {
        return mInnerLayout.getHeight();
    }

    // 开始下拉时的回调
    @Override
    public void pullToRefresh() {
        Log.e("pullToRefresh", "pullToRefresh: 开始下啦");
    }

    // "加载头部"完全显示时的回调
    @Override
    public void releaseToRefresh() {

        if (animCat == null) {
            mCatImage.setImageResource(R.drawable.refreshing_header_anim);
            animCat = (AnimationDrawable) mCatImage.getDrawable();
        }
        animCat.start();
    }

    // 下拉拖动时的回调
    @Override
    public void onPull(float scaleOfLayout) {

    }

    // 释放后刷新时的回调
    @Override
    public void refreshing() {
    }

    // 初始化到未刷新状态
    @Override
    public void reset() {
        if (animCat != null) {
            animCat.stop();
            animCat = null;
        }
        mCatImage.setImageResource(R.mipmap.loading_01);
    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {
    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {
    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {
    }
}
