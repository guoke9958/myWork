package com.xa.qyw.demo;

import java.util.UUID;

import com.xa.qyw.otherweb.rongyun.ApiHttpClient;
import com.xa.qyw.otherweb.rongyun.models.FormatType;
import com.xa.qyw.otherweb.rongyun.models.SdkHttpResult;

public class Test {
	
	public static String key = "x18ywvqf8f9ic";//�滻������appkey
	public static String secret = "1GQeM8EsvYUX";//�滻��ƥ������key��secret
	
	public static void main(String[] args) {
		
		String uuid = UUID.randomUUID().toString().replace("-", "");
		
		System.out.println(uuid);
		
		
//		List<String> list = new ArrayList<String>();
//		list.add("e376bae7506c48f480b8048b496ae2ca");
//		try {
//		
////		SdkHttpResult result = ApiHttpClient.publishMessage(key, secret, "����", list,
////				new TxtMessage("txtMessagehaha"), FormatType.json);
//		
//		
//		
//			SdkHttpResult result = ApiHttpClient.publishSystemMessage(key, secret, "ϵͳ��Ϣ",
//					list, new TxtMessage("��ӭʹ��ҽ��ͨAPP��Ϊ���ṩ���ʵķ��������ǵ����Σ�"), "pushContent",
//					"pushData", FormatType.json);
//			
//			System.out.println(result.getResult());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//����û�����״̬
//				SdkHttpResult result;
//				try {
//					result = ApiHttpClient.checkOnline(key, secret, "b0cc87d53e0e42279ab90d1e376569ef", FormatType.json);
//					System.out.println("checkOnline=" + result);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		
		
	}
}
