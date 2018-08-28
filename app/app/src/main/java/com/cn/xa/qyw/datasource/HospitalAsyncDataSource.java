package com.cn.xa.qyw.datasource;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.entiy.AddDepartments;
import com.cn.xa.qyw.http.AsyncRequestHandle;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

import java.util.List;

/**
 * Created by Administrator on 2016/7/3.
 */
public class HospitalAsyncDataSource implements IAsyncDataSource<List<AddDepartments>> {
    @Override
    public RequestHandle refresh(ResponseSender<List<AddDepartments>> sender) throws Exception {
        return loadDoctor(sender);
    }

    private RequestHandle loadDoctor(final ResponseSender<List<AddDepartments>> sender) {

        return new AsyncRequestHandle(HttpUtils.postDataFromServer(HttpAddress.GET_ALL_DEPARTMENT, new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                sender.sendError(new Exception("请求错误"));
            }

            @Override
            public void onSuccess(String data) {
                List<AddDepartments> doctors = JSONObject.parseArray(data, AddDepartments.class);
                sender.sendData(doctors);
            }
        }));
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<AddDepartments>> sender) throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
