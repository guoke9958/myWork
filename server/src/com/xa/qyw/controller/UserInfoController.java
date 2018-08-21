package com.xa.qyw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.HospitalDetailInfo;
import com.xa.qyw.entiy.UpdateHospitalName;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.otherweb.rongyun.RongYunUtils;
import com.xa.qyw.service.HospitalService;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.tools.mail.SendMail;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.StringUtils;

@Controller
@RequestMapping("/api/userinfo/")
public class UserInfoController {

	@Autowired
	UserInfoService mUserInfoService;

	@Autowired
	HospitalService mHospitalService;

	@ResponseBody
	@RequestMapping("query_user_info")
	public String getQueryUserInfo(@RequestParam(value = "data") String data) {
		UserInfo user = null;
		int result = ResponseUtils.FAIL;
		try {

			String id = "";
			if (data.contains(";")) {
				String[] ids = data.split(";");

				if (StringUtils.isEmpty(ids[0])) {
					id = ids[1];
				} else {
					if (!ids[0].equals("91838ea9d9854e47a13a032cae8c2845")) {
						id = ids[0];
					} else {
						id = ids[1];
					}
				}

			} else {
				id = data;
			}

			user = mUserInfoService.getUserInfoById(id);
			if (user != null) {

				if (42666 == user.getHospitalId()) {
					user.setUserId("91838ea9d9854e47a13a032cae8c2845");
				}

				String token = RongYunUtils.getToken(user.getUserId(), "", "");
				user.setToken(token);
				user.setState(RongYunUtils.getUserState(user.getUserId()));
			}
			result = ResponseUtils.SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result,
				JSONObject.toJSONString(user));
	}

	@ResponseBody
	@RequestMapping("update_photo")
	public String updateUserInfoPhoto(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {
			mUserInfoService.updateUserInfoPhoto(JSONObject.parseObject(data,
					UserInfo.class));
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result, "");
	}

	@ResponseBody
	@RequestMapping("update_user_info")
	public String updateUserInfoAll(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {
			UserInfo info = JSONObject.parseObject(data, UserInfo.class);
			mUserInfoService.updateUserInfoAll(info);
			result = ResponseUtils.SUCCESS;
			queryHospital(info);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result, "");
	}

	@ResponseBody
	@RequestMapping("add")
	public String addUserInfoAll(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {
			UserInfo info = JSONObject.parseObject(data, UserInfo.class);

			UserInfo localInfo = mUserInfoService.getUserInfoById(info
					.getUserId());
			if (localInfo == null) {
				mUserInfoService.insertUserInfo(info);
			} else {
				mUserInfoService.updateUserInfoAll(info);
			}

			result = ResponseUtils.SUCCESS;
			queryHospital(info);
			if (info.getHospitalId() != 0) {
				SendMail.sendAddDoctor(data);
			} else {
				SendMail.sendAddUser(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result, "");
	}

	private void queryHospital(UserInfo userInfo) {
		try {

			HospitalDetailInfo hospital = mHospitalService
					.getHospitalById(userInfo.getHospitalId());

			if (hospital != null && hospital.getIs_pass() == 0) {
				UpdateHospitalName update = new UpdateHospitalName();
				update.setHospitalId(userInfo.getHospitalId() + "");
				update.setHospitalName(userInfo.getHospitalName());
				update.setUserId(userInfo.getUserId());
				update.setIsPass(0);
				update.setHospitalGrade(userInfo.getHospitalGrade());
				mHospitalService.insertUpdateHospitalName(update);
				SendMail.updateHospitalGrade(JSONObject.toJSONString(userInfo));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
