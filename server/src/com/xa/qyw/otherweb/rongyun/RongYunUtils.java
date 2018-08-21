package com.xa.qyw.otherweb.rongyun;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.otherweb.rongyun.models.FormatType;
import com.xa.qyw.otherweb.rongyun.models.ImgMessage;
import com.xa.qyw.otherweb.rongyun.models.ImgTextMessage;
import com.xa.qyw.otherweb.rongyun.models.SdkHttpResult;
import com.xa.qyw.otherweb.rongyun.models.TxtMessage;

public class RongYunUtils {
	public static String key = "x4vkb1qpvpj5k";// 替换成您的appkey
	public static String secret = "wBwrwMWrsCVcpu";// 替换成匹配上面key的secret

	public static String getToken(String userId, String userName,
			String photoUrl) {
		try {
			SdkHttpResult result = ApiHttpClient.getToken(key, secret, userId,
					userName, photoUrl, FormatType.json);

			if (result.getHttpCode() == 200) {
				JSONObject json = JSONObject.parseObject(result.getResult());
				String token = json.getString("token");

				return token;
			}

			System.out.println(result.getResult());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "登录出现异常";
	}

	public static String publishWelcomeSystemMessage(String userId) {

		try {
			List<String> list = new ArrayList<String>();
			list.add(userId);

			SdkHttpResult result = ApiHttpClient.publishSystemMessage(key,
					secret, "系统消息", list, new TxtMessage(
							"欢迎使用医患通APP，为您提供优质的服务是我们的责任！"), "pushContent",
					"pushData", FormatType.json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "200";
	}

	public static String publishWelcomeSystemMessage(List<String> list,
			String msg) {

		try {
			SdkHttpResult result = ApiHttpClient.publishSystemMessage(key,
					secret, "系统消息", list, new TxtMessage(msg), "pushContent",
					"pushData", FormatType.json);

			if (result != null) {
				System.out.println(result.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "200";
	}
	
	public static String publishWelcomeSystemMessage(String userId,
			String msg) {

		try {
			List<String> list = new ArrayList<String>();
			list.add(userId);
			
			SdkHttpResult result = ApiHttpClient.publishSystemMessage(key,
					secret, "系统消息", list, new TxtMessage(msg), "pushContent",
					"pushData", FormatType.json);

			if (result != null) {
				System.out.println(result.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "200";
	}

	
	public static String publishWelcomeSystemImageMessage(String userId,
			String msg,String path) {

		try {
			List<String> list = new ArrayList<String>();
			list.add(userId);
			
			SdkHttpResult result = ApiHttpClient.publishSystemMessage(key,
					secret, "系统消息", list, new ImgTextMessage(msg,"图文消息",path), "pushContent",
					"pushData", FormatType.json);

			if (result != null) {
				System.out.println(result.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "200";
	}
	

	public static String publishMessage(String userId, String msg,
			String fromUserId) {

		try {
			List<String> list = new ArrayList<String>();
			list.add(userId);

			SdkHttpResult result = ApiHttpClient.publishMessage(key, secret,
					fromUserId, list, new TxtMessage(msg), FormatType.json);
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "200";
	}
	
	
	public static String publishImageMessage(String userId, String msg,String url,
			String fromUserId) {

		try {
			List<String> list = new ArrayList<String>();
			list.add(userId);

			SdkHttpResult result = ApiHttpClient.publishMessage(key, secret,
					fromUserId, list, new ImgTextMessage(msg,"图文消息",url) ,FormatType.json);
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "200";
	}
	
	
	public static String publishMessage(List<String> list, String msg,
			String fromUserId) {

		try {

			SdkHttpResult result = ApiHttpClient.publishMessage(key, secret,
					fromUserId, list, new TxtMessage(msg), FormatType.json);
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "200";
	}
	

	public static String getUserState(String userId) {
		SdkHttpResult result;
		try {
			result = ApiHttpClient.checkOnline(key, secret, userId,
					FormatType.json);
			JSONObject json = JSONObject.parseObject(result.getResult());
			return json.getString("status");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

}
