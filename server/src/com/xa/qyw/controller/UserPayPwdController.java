package com.xa.qyw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.UserPayPwd;
import com.xa.qyw.service.UserPayPwdService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/paypwd/")
public class UserPayPwdController {

	@Autowired
	UserPayPwdService mUserPayPwdService;

	@ResponseBody
	@RequestMapping("query")
	public String getUserPayPwd(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		String des = "";
		try {
			UserPayPwd pwd = mUserPayPwdService.getUserPayPwdByUserId(data);
			if (pwd != null) {
				result = ResponseUtils.SUCCESS;
			} else {
				des = "������";
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				"", des);
	}

	@ResponseBody
	@RequestMapping("addAndUpdate")
	public String addAndUpdateUserPayPwd(
			@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {

			UserPayPwd pwd = JSONObject.parseObject(data, UserPayPwd.class);
			pwd.setTryCount(0);

			UserPayPwd localPwd = mUserPayPwdService.getUserPayPwdByUserId(pwd
					.getUserId());
			if (localPwd == null) {
				int res = mUserPayPwdService.addPayPwd(pwd);
				if (res == 1) {
					result = ResponseUtils.SUCCESS;
				}

			} else {
				int res = mUserPayPwdService.updatePayPwd(pwd);
				if (res == 1) {
					result = ResponseUtils.SUCCESS;
				}
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				"");
	}

	@ResponseBody
	@RequestMapping("check")
	public String checkUserPayPwd(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		String des = "";
		try {

			UserPayPwd pwd = JSONObject.parseObject(data, UserPayPwd.class);

			UserPayPwd localPwd = mUserPayPwdService.checkUserPayPwd(pwd);

			if (localPwd != null) {

				if (localPwd.getTryCount() >= 5) {
					des = "֧�������ѱ������������Զ���������Ҳ���Ե���һ����밴ť��ͨ��������������������";
				} else {
					localPwd.setTryCount(0);
					mUserPayPwdService.updatePayErrorCount(localPwd);
					result = ResponseUtils.SUCCESS;
				}

			} else {

				UserPayPwd errorPwd = mUserPayPwdService
						.getUserPayPwdByUserId(pwd.getUserId());

				int errorCount = errorPwd.getTryCount() + 1;

				if (errorCount == 5) {
					des = "֧�������ѱ������������Զ���������Ҳ���Ե���һ����밴ť��ͨ��������������������";
				} else {
					des = "�����ջ���"
							+ (5 - errorCount) + "�γ��Ի���";
				}
				
				errorPwd.setTryCount(errorCount);
				mUserPayPwdService.updatePayErrorCount(errorPwd);
				
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				"", des);
	}
}
