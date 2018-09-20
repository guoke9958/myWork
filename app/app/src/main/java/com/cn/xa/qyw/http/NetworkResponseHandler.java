package com.cn.xa.qyw.http;

import android.util.Base64;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.entiy.HospitalGrade;
import com.cn.xa.qyw.entiy.SimpleBean;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/7/3.
 */
public abstract class NetworkResponseHandler extends AsyncHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String result = new String(responseBody,"UTF-8");
            Lg.e(result);
            SimpleBean bean = JSONObject.parseObject(result, SimpleBean.class);

            if(bean.getResult()==0){
                onSuccess(bean.getData());
            }else {
                onFail(bean.getDescription());
            }
        } catch (Exception e) {
            try {
                String result = new String(responseBody,"UTF-8").replace("\\\"","\"").replace("\\\"","\"");
                result = result.substring(1, result.length()-1);
                Lg.e("解析错误,处理后",result);
                SimpleBean bean = JSONObject.parseObject(result, SimpleBean.class);

                if(bean.getResult()==0){
                    onSuccess(bean.getData());
                }else {
                    onFail(bean.getDescription());
                }
            } catch (Exception e1) {
                onFail("解析错误");
            }
        }
    }





    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onFail("网络异常");
    }

    public abstract void onFail(String message);

    public abstract void onSuccess(String data);

}
