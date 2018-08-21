package com.cn.xa.qyw.utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2017/4/17.
 */
public class ViewUtils {

    public <T extends View> T findViewById(Activity activity,int resId){
        return (T)activity.findViewById(resId);
    }

    public <T extends View> T findViewById(View view,int resId){
        return (T)view.findViewById(resId);
    }
}
