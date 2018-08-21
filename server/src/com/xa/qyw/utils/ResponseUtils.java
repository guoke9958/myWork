package com.xa.qyw.utils;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.entiy.SimpleBean;

public class ResponseUtils {

	public static final String BIZ_CODE = "bizCode";

	public static final String DATA = "data";

	public static final String RESULT = "result";

	public static final Integer FAIL = -1;

	public static final Integer SUCCESS = 0;

	public static String createResponse(Integer bizCode, Integer result,
			String data) {

		SimpleBean bean = new SimpleBean();
		bean.setBizCode(bizCode);
		bean.setResult(result);

		if (StringUtils.isEmpty(data)) {
			data = "";
		}

		bean.setData(data);

		String response = JSONObject.toJSONString(bean);
		System.out.println(response);
		return response;
	}
	
	public static String createResponse(Integer bizCode, Integer result,
			String data,String description) {

		SimpleBean bean = new SimpleBean();
		bean.setBizCode(bizCode);
		bean.setResult(result);
		bean.setDescription(description);

		if (StringUtils.isEmpty(data)) {
			data = "";
		}

		bean.setData(data);

		String response = JSONObject.toJSONString(bean);
		System.out.println(response);
		return response;
	}

}
