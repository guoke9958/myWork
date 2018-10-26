package com.cn.xa.qyw.ui.news.wrapRecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.library.PullToRefreshBase;


/**
 * 自定义 结合  PullToRefresh 1.0.7   自定义刷新
 *              WrapRecyclerView      动态添加 RecyclerView 的头尾
 * Created by amoldZhang on 2018/10/1.
 */
public class HeadPullRecyclerView extends PullToRefreshBase<WrapRecyclerView> {

    public HeadPullRecyclerView(Context context) {
        super(context);
    }

    public HeadPullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadPullRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public HeadPullRecyclerView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected WrapRecyclerView createRefreshableView(Context context,
                                                     AttributeSet attrs) {
        WrapRecyclerView recyclerView = new WrapRecyclerView(context, attrs);
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return isLastItemVisible();
    }

    /**
     *  @Description: 判断第一个条目是否完全可见
     * @return boolean：
     */
    private boolean isFirstItemVisible() {
        final WrapRecyclerView.Adapter<?> adapter = getRefreshableView().getAdapter();

        // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            if (DEBUG) {
                Log.d(LOG_TAG, "isFirstItemVisible. Empty View.");
            }
            return true;

        } else {
            // 第一个条目完全展示,可以刷新
            if (getFirstVisiblePosition() == 0) {
                return mRefreshableView.getChildAt(0).getTop() >= mRefreshableView
                        .getTop();
            }
        }

        return false;
    }

    /**
     * @Description: 获取第一个可见子View的位置下标
     *
     * @return int: 位置
     */
    private int getFirstVisiblePosition() {
        View firstVisibleChild = mRefreshableView.getChildAt(0);
        return firstVisibleChild != null ? mRefreshableView
                .getChildAdapterPosition(firstVisibleChild) : -1;
    }

    /**
     * @Description: 判断最后一个条目是否完全可见
     *
     * @return boolean:
     */
    private boolean isLastItemVisible() {
        final WrapRecyclerView.Adapter<?> adapter = getRefreshableView().getAdapter();

        // 如果未设置Adapter或者Adapter没有数据可以上拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            if (DEBUG) {
                Log.d(LOG_TAG, "isLastItemVisible. Empty View.");
            }
            return true;

        } else {
            // 最后一个条目View完全展示,可以刷新
            int lastVisiblePosition = getLastVisiblePosition();
            if(lastVisiblePosition >= mRefreshableView.getAdapter().getItemCount()-1) {
                return mRefreshableView.getChildAt(
                        mRefreshableView.getChildCount() - 1).getBottom() <= mRefreshableView
                        .getBottom();
            }
        }

        return false;
    }

    /**
     * @Description: 获取最后一个可见子View的位置下标
     *
     * @return int: 位置
     */
    private int getLastVisiblePosition() {
        View lastVisibleChild = mRefreshableView.getChildAt(mRefreshableView
                .getChildCount() - 1);
        return lastVisibleChild != null ? mRefreshableView
                .getChildAdapterPosition(lastVisibleChild) : -1;
    }
}
