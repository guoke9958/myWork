package com.cn.xa.qyw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by cn on 15-6-12.
 */
public class DoctorGridView extends GridView {
    public DoctorGridView(Context context) {
        super(context);
    }

    public DoctorGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoctorGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
