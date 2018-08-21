package com.xa.qyw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.UserAliAccount;
import com.xa.qyw.service.UserAliAccountService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/userAli/")
public class UserAliAccountController {

	@Autowired
	UserAliAccountService mUserAliAccountService;

	@ResponseBody
	@RequestMapping("get")
	public String getUserAliAccount(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		List<UserAliAccount> list = null;
		try {
			list = mUserAliAccountService.getUserAliAccountByUserId(data);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.LOGIN, result,
				JSONObject.toJSONString(list));
	}

	@ResponseBody
	@RequestMapping("add")
	public String addUserAliAccount(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		UserAliAccount userAli = null;
		String des = "添加失败";
		try {
			userAli = JSONObject.parseObject(data, UserAliAccount.class);

			UserAliAccount localiUserAli = mUserAliAccountService
					.queryUserAliAccount(userAli);

			if (localiUserAli == null) {
				mUserAliAccountService.addUserAliAccount(userAli);
				result = ResponseUtils.SUCCESS;
			} else {
				des = "此账户已经添加过";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.LOGIN, result,
				JSONObject.toJSONString(userAli), des);
	}

	@ResponseBody
	@RequestMapping("delete")
	public String deleteUserAliAccount(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {

			mUserAliAccountService.deleteUserAliAccount(Integer.parseInt(data));
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.LOGIN, result,"");
	}

}
