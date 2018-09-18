package com.cn.xa.qyw.http;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.entiy.SimpleBean;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/7/3.
 */
public abstract class NetworkResponseHandler extends AsyncHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String result = new String(responseBody).replace("\"\"","").replace("\\\\","\\");
        Lg.e(result);
        SimpleBean bean = JSONObject.parseObject(result, SimpleBean.class);

        if(bean.getResult()==0){
            onSuccess(bean.getData());
        }else {
            onFail(bean.getDescription());
        }

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onFail("网络异常");
    }

    public abstract void onFail(String message);

    public abstract void onSuccess(String data);

}
