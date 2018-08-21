package com.xa.qyw.utils;

import java.util.UUID;

import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.User;
import com.xa.qyw.entiy.UserCapital;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.otherweb.rongyun.RongYunUtils;
import com.xa.qyw.service.UserCapitalService;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.service.UserService;

public class UserUtils {

	public static void addUser(UserService mUserService,
			UserInfoService mUserInfoService,
			UserCapitalService mUserCapitalService, String phone, String name,
			String userPhoto,String province,String city,String area,int id) {
		try {

			if (StringUtils.isTelActive(phone)) {

				User user = new User();
				user.setPassword(MD5Util.encryptMD5("123456"));
				user.setType(6);
				user.setUserName(phone);

				SimpUser simUser = mUserService.getUserByUserName(phone);

				if (simUser == null) {
					user.setUserId(UUID.randomUUID().toString()
							.replace("-", ""));
					String token = RongYunUtils.getToken(user.getUserId(), "",
							"");
					user.setToken(token);
					RongYunUtils.publishWelcomeSystemMessage(user.getUserId());
					mUserService.addUser(user);

					UserCapital capital = new UserCapital();
					capital.setId(0);
					capital.setUserId(user.getUserId());
					capital.setExpandCapital(0);
					capital.setRechargeCapital(0);
					capital.setIncomeCapital(0);
					capital.setCapitalTotal(0);
					capital.setUpdateTime(DateUtils.getCurrentTimestamp());
					mUserCapitalService.insertUserCapital(capital);

					UserInfo userInfo = new UserInfo();
					userInfo.setUserId(user.getUserId());
					userInfo.setNickName(name);
					userInfo.setTrueName(name);
					userInfo.setType(user.getType());
					userInfo.setPhoto(userPhoto);
					userInfo.setPhone(phone);
					userInfo.setProvince(province);
					userInfo.setCity(city);
					userInfo.setArea(area);
					userInfo.setDepartmentId(id);

					mUserInfoService.insertUserInfo(userInfo);

					System.out.println("Ìí¼Ó³É¹¦");

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
