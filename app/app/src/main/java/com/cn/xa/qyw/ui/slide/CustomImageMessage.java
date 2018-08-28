package com.cn.xa.qyw.ui.slide;

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
 * Created by Administrator on 2016/9/22.
 */
@MessageTag(value = "app:customImageMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CustomImageMessage extends MessageContent {

    private String content;
    private String imagePath;
    private String toUserId;

    protected CustomImageMessage() {
    }

    public static CustomImageMessage obtain(String text, String imagePath, String mTagId) {
        CustomImageMessage model = new CustomImageMessage();
        model.setContent(text);
        model.setImagePath(imagePath);
        model.setToUserId(mTagId);
        return model;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public CustomImageMessage(byte[] data) {
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

            if (jsonObj.has("imagePath"))
                imagePath = jsonObj.optString("imagePath");

            if (jsonObj.has("toUserId"))
                toUserId = jsonObj.optString("toUserId");

        } catch (JSONException e) {
            Lg.e("JSONException" + e.getMessage());
        }

    }

    //给消息赋值。
    public CustomImageMessage(Parcel in) {
        content= ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        imagePath = ParcelUtils.readFromParcel(in);
        toUserId = ParcelUtils.readFromParcel(in);
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", content);
            jsonObj.put("imagePath",imagePath);
            jsonObj.put("toUserId",toUserId);
        } catch (JSONException e) {
            Log.e("chengnan", "JSONException" + e.getMessage());
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
        ParcelUtils.writeToParcel(dest, imagePath);
        ParcelUtils.writeToParcel(dest, toUserId);
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<CustomImageMessage> CREATOR = new Creator<CustomImageMessage>() {

        @Override
        public CustomImageMessage createFromParcel(Parcel source) {
            return new CustomImageMessage(source);
        }

        @Override
        public CustomImageMessage[] newArray(int size) {
            return new CustomImageMessage[size];
        }
    };
}
