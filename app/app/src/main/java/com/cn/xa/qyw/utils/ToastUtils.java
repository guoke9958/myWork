package com.cn.xa.qyw.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Created by cn on 15-6-1.
 */
public class ToastUtils {

    public static void showShortToast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context,int resourceId){
        Toast.makeText(context,resourceId,Toast.LENGTH_SHORT).show();
    }


    public static void showShortSnackbar(View view,String content){
       showShortToast(view.getContext(),content);
    }

    public static void showShortSnackbar(View view,int resourceId){
        showShortToast(view.getContext(),resourceId);
    }

}
