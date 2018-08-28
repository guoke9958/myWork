package com.cn.xa.qyw.ui.chat;

import android.os.Parcel;
import android.util.Log;

import com.cn.xa.qyw.utils.Lg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.push.common.ParcelUtils;

/**
 * Created by Administrator on 2016/9/18.
 */

@MessageTag(value = "app:hongbao", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class HongBaoMessage extends MessageContent {


    private String content;
    private int count;
    private String orderId;
    private String toUserId;

    protected HongBaoMessage() {
    }

    public static HongBaoMessage obtain(String text,int count,String orderId,String toUserId) {
        HongBaoMessage model = new HongBaoMessage();
        model.setContent(text);
        model.setCount(count);
        model.setOrderId(orderId);
        model.setToUserId(toUserId);
        return model;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public HongBaoMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                content = jsonObj.optString("content");

            if(jsonObj.has("count")){
                count = jsonObj.optInt("count");
            }

            if(jsonObj.has("orderId")){
                orderId = jsonObj.optString("orderId");
            }

            if(jsonObj.has("toUserId")){
                toUserId = jsonObj.optString("toUserId");
            }

        } catch (JSONException e) {
            Lg.e("JSONException"+e.getMessage());
        }

    }


    //给消息赋值。
    public HongBaoMessage(Parcel in) {
        content= ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        count = ParcelUtils.readIntFromParcel(in);
        orderId = ParcelUtils.readFromParcel(in);
        toUserId = ParcelUtils.readFromParcel(in);
        //这里可继续增加你消息的属性
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", content);
            jsonObj.put("count",count);
            jsonObj.put("orderId",orderId);
            jsonObj.put("toUserId",toUserId);
        } catch (JSONException e) {
            Log.e("chengnan","JSONException"+e.getMessage());
        }

        Lg.e("========"+jsonObj.toString());

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);//该类为工具类，对消息中属性进行序列化
        ParcelUtils.writeToParcel(dest,count);
        ParcelUtils.writeToParcel(dest,orderId);
        ParcelUtils.writeToParcel(dest,toUserId);

    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<HongBaoMessage> CREATOR = new Creator<HongBaoMessage>() {

        @Override
        public HongBaoMessage createFromParcel(Parcel source) {
            return new HongBaoMessage(source);
        }

        @Override
        public HongBaoMessage[] newArray(int size) {
            return new HongBaoMessage[size];
        }
    };
}
