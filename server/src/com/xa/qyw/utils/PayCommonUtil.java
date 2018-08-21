package com.xa.qyw.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class PayCommonUtil {
	
	 public static String createSign(String characterEncoding,SortedMap<String,String> parameters){
	        StringBuffer sb = new StringBuffer();
	        Set es = parameters.entrySet();
	        Iterator it = es.iterator();
	        while(it.hasNext()) {
	            Map.Entry entry = (Map.Entry)it.next();
	            String k = (String)entry.getKey();
	            Object v = entry.getValue();
	            if(null != v && !"".equals(v)
	                    && !"sign".equals(k) && !"key".equals(k)) {
	                sb.append(k + "=" + v + "&");
	            }
	        }
	        sb.append("key=" + ConstantUtil.PARTNER_KEY);//������ʱ����̻���Կ������keyֵ����������Բ�����ӵ�SortMap����ȥ�������������뷽ʽ����UTF-8
	        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	        return sign;
	    }

}
