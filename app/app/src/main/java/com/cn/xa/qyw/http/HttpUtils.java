package com.cn.xa.qyw.http;

import android.content.Context;

import com.cn.xa.qyw.utils.Lg;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/5/11.
 */
public class HttpUtils {

    private static final AsyncHttpClient client;

    static {
        client = new AsyncHttpClient();
        client.setTimeout(12000);
        client.setMaxRetriesAndTimeout(1,1000);
    }

    public static RequestHandle getDataFromServer(String url, AsyncHttpResponseHandler handler) {
        Lg.e(url);
        return client.get(url, handler);
    }

    public static RequestHandle getDataFromServer(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        Lg.e(url);
        Lg.e(params.toString());
        return client.get(url, params, handler);
    }

    public static RequestHandle postDataFromServer(String url, AsyncHttpResponseHandler handler){
        Lg.e(url);
        return client.post(url, handler);
    }

    public static RequestHandle postDataFromServer(String url, RequestParams params, AsyncHttpResponseHandler handler){
        Lg.e(url);
        Lg.e(params.toString());
        return client.post(url, params, handler);
    }

    public static RequestHandle postDataFromServer(String url, String data, AsyncHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("data",data);
        Lg.e(url);
        Lg.e(data);
        return client.post(url, params, handler);
    }

    public static String sendGet(String url, String userId) throws IOException {
        StringBuffer buffer = new StringBuffer(); //用来拼接参数
        StringBuffer result = new StringBuffer(); //用来接受返回值
        URL httpUrl = null; //HTTP URL类 用这个类来创建连接
        URLConnection connection = null; //创建的http连接
        BufferedReader bufferedReader = null; //接受连接受的参数
        //如果存在参数，我们才需要拼接参数 类似于 localhost/index.html?a=a&b=b
        url = url + "?data=" +userId ;
        //创建URL
        httpUrl = new URL(url);
        //建立连接
        connection = httpUrl.openConnection();
        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connection.setRequestProperty("connection", "keep-alive");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        connection.connect();
        //接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }

    public static RequestHandle downloadFile(Context context,String url,BinaryHttpResponseHandler handler){
        Lg.e("url==========" + url);
        return client.get(context,url,handler);
    }

    public static void cancelAllRequest(Context context){
        client.cancelRequests(context,true);
    }

}
