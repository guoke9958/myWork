package com.xa.qyw.otherweb.xinge;

import com.alibaba.fastjson.JSONObject;
import com.tencent.xinge.Message;
import com.tencent.xinge.XingeApp;
import com.xa.qyw.entiy.Push;

public class XinGeUtils {

	public static long ID = 2100257388;
	private static String KEY = "1222817571781d42e73741f994e17b34";

	public static org.json.JSONObject sendPushXinGe(Push push) {
		try {
			XingeApp xinge = new XingeApp(ID, KEY);

			Message mess = new Message();
			mess.setExpireTime(86400);
			mess.setTitle("Ω°øµ”Î…˙ªÓ");
			mess.setType(Message.TYPE_MESSAGE);
			mess.setContent(JSONObject.toJSONString(push));
			org.json.JSONObject ret = xinge.pushAllDevice(0, mess);
			System.out.println(ret);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
