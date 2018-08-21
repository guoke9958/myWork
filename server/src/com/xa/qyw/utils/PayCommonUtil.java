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
	        sb.append("key=" + ConstantUtil.PARTNER_KEY);//最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
	        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	        return sign;
	    }

}
