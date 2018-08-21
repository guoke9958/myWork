package com.cn.xa.qyw.datasource;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.entiy.Shop;
import com.cn.xa.qyw.entiy.ShopType;
import com.cn.xa.qyw.http.AsyncRequestHandle;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;

/**
 * Created by 409160 on 2016/7/28.
 */
public class ShopAsyncDataSource implements IAsyncDataSource<Shop> {

    private int type1,type2,type3;

    @Override
    public RequestHandle refresh(ResponseSender<Shop> sender) throws Exception {
        String showType1String = PreferenceUtils.getPrefString(PreferenceKeys.SHOW_TYPE_1,"");
        String showType2String = PreferenceUtils.getPrefString(PreferenceKeys.SHOW_TYPE_2,"");
        String showType3String = PreferenceUtils.getPrefString(PreferenceKeys.SHOW_TYPE_3,"");

        if(StringUtils.isEmpty(showType1String)){
            type1 = 0;
        }else{
            ShopType type = JSONObject.parseObject(showType1String, ShopType.class);
            type1 = type.getId();
        }

        if(StringUtils.isEmpty(showType2String)){
            type2 = 0;
        }else{
            ShopType type = JSONObject.parseObject(showType1String, ShopType.class);
            type2 = type.getId();
        }

        if(StringUtils.isEmpty(showType3String)){
            type3 = 0;
        }else{
            ShopType type = JSONObject.parseObject(showType1String, ShopType.class);
            type3 = type.getId();
        }

        ShopType type = new ShopType();
        type.setId(type1);
        type.setFirstId(type2);
        type.setSecondId(type3);

        return refreshLoad(sender,type);
    }

    private RequestHandle refreshLoad(final ResponseSender<Shop> sender, ShopType type) {
        return new AsyncRequestHandle(HttpUtils.postDataFromServer(HttpAddress.GET_SHOP_TYPE, JSONObject.toJSONString(type), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                sender.sendError(new Exception("访问异常"));
            }

            @Override
            public void onSuccess(String data) {
                sender.sendData(JSONObject.parseObject(data,Shop.class));
            }
        }));
    }

    @Override
    public RequestHandle loadMore(ResponseSender<Shop> sender) throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
