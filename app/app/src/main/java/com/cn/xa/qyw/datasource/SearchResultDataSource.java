package com.cn.xa.qyw.datasource;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.entiy.SearchItem;
import com.cn.xa.qyw.entiy.UserInfo;
import com.cn.xa.qyw.http.AsyncRequestHandle;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

import java.util.List;

/**
 * Created by 409160 on 2017/1/16.
 */
public class SearchResultDataSource implements IAsyncDataSource<List<UserInfo>> {

    private String mKey;
    private boolean mIsMore;
    private UserInfo mUserInfo;

    public SearchResultDataSource() {
    }

    public void setKey(String key) {
        mKey = "%" + key + "%";
    }

    @Override
    public RequestHandle refresh(ResponseSender<List<UserInfo>> sender) throws Exception {

        SearchItem searchItem = new SearchItem(mKey, DateUtils.getCurrentTimestamp());
        searchItem.setFirst(true);
        return getData(searchItem, sender);
    }

    private RequestHandle getData(SearchItem searchItem, final ResponseSender<List<UserInfo>> sender) {

        if(DoctorApplication.mUser!=null){
            searchItem.setUserId(DoctorApplication.mUser.getUserId());
        }
        return new AsyncRequestHandle(HttpUtils.postDataFromServer(HttpAddress.SEARCH, JSONObject.toJSONString(searchItem), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                sender.sendError(new Exception("请求错误"));
            }

            @Override
            public void onSuccess(String data) {
                List<UserInfo> info = JSONObject.parseArray(data, UserInfo.class);

                if (info != null && info.size() == 20) {
                    mIsMore = true;
                    mUserInfo = info.get(info.size() - 1);
                } else {
                    mIsMore = false;
                }

                sender.sendData(info);
            }
        }));
    }

    @Override
    public RequestHandle loadMore(ResponseSender<List<UserInfo>> sender) throws Exception {
        return getData(new SearchItem(mKey, mUserInfo.getCreateTime()), sender);
    }

    @Override
    public boolean hasMore() {
        return mIsMore;
    }
}
