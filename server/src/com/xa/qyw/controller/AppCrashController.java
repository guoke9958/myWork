package com.xa.qyw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.AppCrash;
import com.xa.qyw.service.AppCrashService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/crash/")
public class AppCrashController {

	@Autowired
	AppCrashService mAppCrashService;

	@ResponseBody
	@RequestMapping("add")
	public String addAppCrash(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {
			AppCrash crash = JSONObject.parseObject(data, AppCrash.class);
			mAppCrashService.insertAppCrash(crash);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.LOGIN, result, "");
	}

}
