package com.xa.qyw.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.MsgState;
import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.SimpleUser;
import com.xa.qyw.entiy.User;
import com.xa.qyw.entiy.UserCapital;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.entiy.Voucher;
import com.xa.qyw.otherweb.note.MsgSend;
import com.xa.qyw.otherweb.rongyun.RongYunUtils;
import com.xa.qyw.service.MsgService;
import com.xa.qyw.service.UserCapitalService;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.service.UserService;
import com.xa.qyw.service.UserVoucherService;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.MD5Util;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.StringUtils;

@Controller
@RequestMapping("/api/user/")
public class UserController {

	@Autowired
	UserService mUserService;

	@Autowired
	UserCapitalService mUserCapitalService;

	@Autowired
	MsgService mMsgService;
	
	@Autowired
	UserVoucherService mUserVoucherService;

	@Autowired
	UserInfoService mUserInfoService;

	@ResponseBody
	@RequestMapping("login")
	public String loginUser(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		User localUser = null;
		String des = "用户不存在";
		try {
			User user = JSONObject.parseObject(data, User.class);

			SimpUser queryUser = mUserService.getUserByUserName(user
					.getUserName());

			if (queryUser != null) {

				localUser = mUserService.getUserById(user);

				if (localUser != null) {

					updateLoginCount(localUser);
					
					String token = RongYunUtils.getToken(localUser.getUserId(),
							"", "");
					localUser.setToken(token);

					result = ResponseUtils.SUCCESS;

					des = "登录成功";
				} else {
					des = "用户名或密码错误";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.LOGIN, result,
				JSONObject.toJSONString(localUser), des);
	}

	private void updateLoginCount(final User localUser) {
		new Thread(){
			public void run() {
				
				if(localUser.getLoginCount()==0){
					
					Voucher voucher = new Voucher();
					voucher.setUserId(localUser.getUserId());
					voucher.setUserVoucher(200);
					voucher.setCreateTime(DateUtils.getCurrentTimestamp());
					mUserVoucherService.insertVoucher(voucher);
					
					RongYunUtils.publishWelcomeSystemMessage(localUser.getUserId(), "注册完善资料即得200元代金券，该代金券不能提现，不能用于购买实物，不能用于任何打赏。只能用于在本系统内推送消息的扣费。目前，每推送一条消息，扣费2元。以后，如有变化，恕不提前通知，谢谢！");
					
				}
				
				localUser.setLoginCount(localUser.getLoginCount()+1);
				mUserService.updateUserLoginCount(localUser);
				
			};
			
		}.start();
	}

	@ResponseBody
	@RequestMapping("query")
	public String queryUser(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		User localUser = null;
		try {
			User user = JSONObject.parseObject(data, User.class);

			localUser = mUserService.queryUser(user.getUserName());

			if (localUser != null) {
				result = ResponseUtils.SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.LOGIN, result,
				JSONObject.toJSONString(localUser));
	}

	@ResponseBody
	@RequestMapping("all")
	public String getAllUser() {

		List<SimpUser> listUser = mUserService.getAllUser();
		// for (SimpUser user : list) {
		//
		// try {
		// UserInfo userInfo = mUserInfoService.getUserInfoById(user
		// .getUserId());
		//
		// if (userInfo == null) {
		// System.out.println(JSONObject.toJSONString(user));
		// }
		//
		// } catch (Exception e) {
		// }

		// }

		List<UserInfo> listInfo = mUserInfoService.getAllUserInfo();
		// int i = 0;
		// for (UserInfo info : listInfo) {
		//
		// for (SimpUser user : listUser) {
		//
		// if (info.getUserId().equals(user.getUserId())) {
		//
		// boolean isTel = StringUtils.isTelActive(info.getPhone());
		//
		// boolean isTelActive = StringUtils.isTelActive(user
		// .getUserName());
		//
		// if (isTelActive) {
		// i++;
		// }
		//
		// }
		//
		// }
		//
		// }

		LinkedHashMap<String, List<UserInfo>> map = new LinkedHashMap<String, List<UserInfo>>();
		int i = 0;
		for (UserInfo user : listInfo) {

			if (map.get(user.getNickName()) == null) {
				List<UserInfo> users = new ArrayList<UserInfo>();

				users.add(user);
				map.put(user.getNickName(), users);
			} else {
				List<UserInfo> users = map.get(user.getNickName());

				users.add(user);
				map.put(user.getNickName(), users);

				for (UserInfo info2 : users) {
					System.out.println("name===" + info2.getNickName() + "===="
							+ info2.getUserId() + "====" + info2.getPhone());

					if (StringUtils.isEmpty(info2.getPhone())) {
						System.out.println("删除成功");
						mUserInfoService.deleteUserInfo(info2.getUserId());
						mUserService.deleteUser(info2.getUserId());
					}

				}

			}

		}

		return JSONObject.toJSONString(listUser);
	}

	@ResponseBody
	@RequestMapping("update_password")
	public String updatePassword(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		try {
			User user = JSONObject.parseObject(data, User.class);
			mUserService.updatePasswrod(user);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.UPDATE_PASS, result, "");
	}

	@ResponseBody
	@RequestMapping("forget_password")
	public String forgetPassword(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		try {
			User user = JSONObject.parseObject(data, User.class);

			SimpUser localUser = mUserService.getUserByUserName(user
					.getUserName());
			if (localUser != null) {
				user.setUserId(localUser.getUserId());
				mUserService.updatePasswrod(user);
				result = ResponseUtils.SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.UPDATE_PASS, result, "");
	}

	@ResponseBody
	@RequestMapping("update_username")
	public String updateUserId(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		User user1 = JSONObject.parseObject(data, User.class);

		SimpUser user = new SimpUser();
		user.setUserId(user1.getUserId());
		user.setUserName(user1.getUserName());

		SimpUser localUser = mUserService
				.getUserByUserName(user1.getUserName());
		if (localUser == null) {
			mUserService.updateUserName(user);
			try {

				if (!"modif".equals(user1.getRequestType())) {
					RongYunUtils.publishWelcomeSystemMessage(user1.getUserId());
				}

				UserInfo info = new UserInfo();
				info.setUserId(user.getUserId());
				info.setPhone(user.getUserName());
				mUserInfoService.updateUserPhone(info);

				result = ResponseUtils.SUCCESS;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ResponseUtils.createResponse(BizCode.UPDATE_USER_ID, result,
				JSONObject.toJSONString(user1));
	}

	@ResponseBody
	@RequestMapping("add")
	public String addUser(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		User user = JSONObject.parseObject(data, User.class);
		try {

			SimpUser simpleUser = mUserService.getUserByUserName(user
					.getUserName());

			if (simpleUser == null) {
				user.setUserId(UUID.randomUUID().toString().replace("-", ""));
				user.setLoginCount(0);
				mUserService.addUser(user);
				String token = RongYunUtils.getToken(user.getUserId(), "", "");
				user.setToken(token);
				RongYunUtils.publishWelcomeSystemMessage(user.getUserId());

				UserCapital capital = new UserCapital();
				capital.setId(0);
				capital.setUserId(user.getUserId());
				capital.setExpandCapital(0);
				capital.setRechargeCapital(0);
				capital.setIncomeCapital(0);
				capital.setCapitalTotal(0);
				capital.setUpdateTime(DateUtils.getCurrentTimestamp());
				mUserCapitalService.insertUserCapital(capital);

				result = ResponseUtils.SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.REGISTER, result,
				JSONObject.toJSONString(user));
	}

	@ResponseBody
	@RequestMapping("get_user_photo")
	public String getUserPhoto(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		try {

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_USER_PHOTO, result, "");
	}

	@ResponseBody
	@RequestMapping("get_auth_code")
	public String getAuthCode(@RequestParam(value = "data") String data,
			HttpServletRequest request) {
		int result = ResponseUtils.FAIL;
		String versionCode = "";
		String des = "发送失败";
		String type = request.getParameter("type");
		try {

			SimpUser user = mUserService.getUserByUserName(data);
			if (user == null) {

				if ("forgetpwd".equals(type)) {
					des = "用户不存在";
				} else {
					String random = MsgSend.getRandomCode();
					long code = MsgSend.sendNoteVersionCode(random, data);
					String info = MsgSend.getMsgSendState(code);
					if ("发送成功".equals(info)) {
						result = ResponseUtils.SUCCESS;
						versionCode = random;
						des = "发送成功";
					} else {
						des = info;
					}

					MsgState state = new MsgState();
					state.setId(0);
					state.setOrderId(code + "");
					state.setPhoneNumber(data);
					state.setRandomCode(random);
					state.setSendState(info);
					state.setIsUsed(0);
					state.setSendTime(DateUtils.getCurrentTimestamp());
					mMsgService.insertMsgSendState(state);
				}

			} else {
				if ("forgetpwd".equals(type)) {
					String random = MsgSend.getRandomCode();
					long code = MsgSend.sendNoteVersionCode(random, data);
					String info = MsgSend.getMsgSendState(code);
					if ("发送成功".equals(info)) {
						result = ResponseUtils.SUCCESS;
						versionCode = random;
						des = "发送成功";
					} else {
						des = info;
					}

					MsgState state = new MsgState();
					state.setId(0);
					state.setOrderId(code + "");
					state.setPhoneNumber(data);
					state.setSendState(info);
					state.setIsUsed(0);
					state.setRandomCode(random);
					state.setSendTime(DateUtils.getCurrentTimestamp());
					mMsgService.insertMsgSendState(state);
				} else {
					des = "用户已存在";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_AUTH_CODE, result,
				versionCode, des);
	}

	@ResponseBody
	@RequestMapping("add_user_info")
	public String addUserInfo(HttpServletRequest request,
			@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		User user = JSONObject.parseObject(data, User.class);
		try {

			SimpleUser simUser = mUserService.getUserName(user.getUserId());
			if (simUser != null) {
				mUserService.updateUserInfo(user);
			} else {
				mUserService.addUserInfo(user);
				String token = RongYunUtils.getToken(user.getUserId(), "", "");
				user.setToken(token);
			}

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result,
				JSONObject.toJSONString(user));
	}

	@ResponseBody
	@RequestMapping("addUserText")
	public String addUserAll() {
		List<String> list = new ArrayList<String>();

		String filePath = "C:/Users/409160/Desktop/求医网/一附院.txt";

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferedreader = new BufferedReader(fr);
			String instring;
			while ((instring = bufferedreader.readLine()) != null) {
				if (0 != instring.length()) {
					if (!StringUtils.isEmpty(instring)) {
						list.add(instring);
					}
				}
			}
			fr.close();

			for (String str : list) {
				if (!StringUtils.isEmpty(str)) {
					str = str.replace("#", "");
					if (str.contains(" ")) {
						String phone = "";
						String name = "";
						String[] arr = str.split(" ");

						for (int i = 0; i < arr.length; i++) {
							if (!StringUtils.isEmpty(arr[i])) {
								if (i == 0) {
									name = arr[i].replace("#", "")
											.replace("\t", "").replace(" ", "");
								} else {
									phone = arr[i].replace("#", "")
											.replace("\t", "").replace(" ", "");
								}
							}
						}

						List<UserInfo> listInfo = mUserInfoService
								.queryUserInfoByName(name);

						if (listInfo == null || listInfo.size() == 0) {

							SimpUser user = mUserService
									.getUserByUserName(phone);

							if (user == null) {
								System.err.println("不存在这个医生===" + name);
							} else {
								System.err.println("通过名字没有找到医生===" + name
										+ "==通过手机号找到了==" + arr[1]);
							}
						} else {

							if (listInfo.size() == 1) {
								UserInfo info1 = listInfo.get(0);
								if (StringUtils.isEmpty(listInfo.get(0)
										.getPhone())) {
									System.out.println("找到一个用户信息===并且手机号为空");

									info1.setPhone(phone);
									mUserInfoService.updateUserPhone(info1);

								}
							} else if (listInfo.size() == 2) {

								UserInfo info1 = listInfo.get(0);
								UserInfo info2 = listInfo.get(1);

								if (info1.getDepartmentId() != 220
										&& info2.getDepartmentId() == 220) {

									info1.setHospitalId(1);
									mUserInfoService
											.updateUserInfoHospital(info1);

									System.err.println("删除第二个用户===="
											+ info1.getHospitalId() + "====="
											+ info2.getHospitalId());

									if (info2.getHospitalId() == info1
											.getHospitalId()) {

										if (StringUtils.isEmpty(info1
												.getPhone())) {
											info1.setPhone(phone);

											SimpUser user = new SimpUser();
											user.setUserId(info1.getUserId());
											user.setUserName(phone);

											mUserService.updateUserName(user);
											mUserInfoService
													.updateUserPhone(info1);

											System.err
													.println("更新登录名称和医生电话信息成功");

										}

										mUserInfoService.deleteUserInfo(info2
												.getUserId());
										mUserService.deleteUser(info2
												.getUserId());

										System.err.println("重复医生删除成功");

									}

								} else if (info2.getDepartmentId() != 220
										&& info1.getDepartmentId() == 220) {
									System.err.println("删除第一个用户");
								} else if (info2.getDepartmentId() == 220
										&& info1.getDepartmentId() == 220) {
									if (info2.getHospitalId() == info1
											.getHospitalId()) {
										System.err.println("删除第一个用户");
										mUserInfoService.deleteUserInfo(info2
												.getUserId());
										mUserService.deleteUser(info2
												.getUserId());

									}
								}

							}

						}

						//
					} else if (str.contains("\t")) {

						String phone = "";
						String name = "";

						String[] arr = str.split("\t");

						for (int i = 0; i < arr.length; i++) {
							if (!StringUtils.isEmpty(arr[i])) {
								if (i == 0) {
									name = arr[i].replace("#", "")
											.replace("\t", "").replace(" ", "");
								} else {
									phone = arr[i].replace("#", "")
											.replace("\t", "").replace(" ", "");
								}
							}
						}

						List<UserInfo> listInfo = mUserInfoService
								.queryUserInfoByName(name);

						if (listInfo == null || listInfo.size() == 0) {

							SimpUser user = mUserService
									.getUserByUserName(phone);

							if (user == null) {
								System.err.println("不存在这个医生===" + name);
							} else {
								System.err.println("通过名字没有找到医生===" + name
										+ "==通过手机号找到了==" + arr[1]);
							}
						} else {

							if (listInfo.size() == 1) {
								UserInfo info1 = listInfo.get(0);
								if (StringUtils.isEmpty(info1.getPhone())) {
									System.out.println("找到一个用户信息===并且手机号为空");

									info1.setPhone(phone);
									mUserInfoService.updateUserPhone(info1);

								}
							} else if (listInfo.size() == 2) {
								UserInfo info1 = listInfo.get(0);
								UserInfo info2 = listInfo.get(1);

								if (info1.getDepartmentId() != 220
										&& info2.getDepartmentId() == 220) {
									if (info2.getHospitalId() == info1
											.getHospitalId()) {

										if (StringUtils.isEmpty(info1
												.getPhone())) {
											info1.setPhone(phone);

											SimpUser user = new SimpUser();
											user.setUserId(info1.getUserId());
											user.setUserName(phone);

											mUserService.updateUserName(user);
											mUserInfoService
													.updateUserPhone(info1);

											System.err
													.println("更新登录名称和医生电话信息成功");

										}

										mUserInfoService.deleteUserInfo(info2
												.getUserId());
										mUserService.deleteUser(info2
												.getUserId());

										System.err.println("重复医生删除成功");

									}

								} else if (info2.getDepartmentId() != 220
										&& info1.getDepartmentId() == 220) {
									System.err.println("删除第一个用户");
								} else if (info2.getDepartmentId() == 220
										&& info1.getDepartmentId() == 220) {

									if (info2.getHospitalId() == info1
											.getHospitalId()) {
										System.err.println("删除第一个用户");

										mUserInfoService.deleteUserInfo(info2
												.getUserId());
										mUserService.deleteUser(info2
												.getUserId());
									}

								}

							}

						}

					} else {
						System.err.println("出现错误信息" + str);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, 0, "");
	}

	private void addUser(String name, String phone) {
		try {
			User user = mUserService.queryUser(phone);
			if (user == null) {

				User user1 = new User();
				user1.setType(1);
				user1.setUserName(phone);
				user1.setPassword(MD5Util.encryptMD5("123456"));
				user1.setUserId(UUID.randomUUID().toString().replace("-", ""));
				mUserService.addUser(user1);

				UserCapital capital = new UserCapital();
				capital.setId(0);
				capital.setUserId(user1.getUserId());
				capital.setExpandCapital(0);
				capital.setRechargeCapital(0);
				capital.setIncomeCapital(0);
				capital.setCapitalTotal(0);
				capital.setUpdateTime(DateUtils.getCurrentTimestamp());
				mUserCapitalService.insertUserCapital(capital);

				UserInfo userinfo = new UserInfo();
				userinfo.setUserId(user1.getUserId());
				userinfo.setNickName(name);
				userinfo.setTrueName(name);
				userinfo.setPhone(phone);
				userinfo.setHospitalId(1);
				userinfo.setIsPass(0);
				userinfo.setNormalAdvisory("50-100");
				userinfo.setPhoneAdvisory("100-200");
				userinfo.setBigAdvisory("100-1000");
				userinfo.setFamilyDoctor("1000-3000");
				userinfo.setHealthManager("100-1000");

				mUserInfoService.insertUserInfo(userinfo);
			} else {
				System.err.println(name + "用户已存在" + phone);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@ResponseBody
	@RequestMapping("addUserText1")
	public String addUserAll1() {
		List<String> list = new ArrayList<String>();

		String filePath = "C:/Users/409160/Desktop/我手机里的30多个医生.txt";

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferedreader = new BufferedReader(fr);
			String instring;
			while ((instring = bufferedreader.readLine()) != null) {
				if (0 != instring.length()) {
					if (!StringUtils.isEmpty(instring)) {
						list.add(instring);
					}
				}
			}
			fr.close();

			for (String str : list) {
				if (!StringUtils.isEmpty(str) && str.contains("#")) {

					str = str.replace(" ", "");

					String[] arr = str.split("#");
					String name = arr[0].replace(" ", "");

					String phone = arr[1].replace(" ", "");

					SimpUser listInfo = mUserService.getUserByUserName(phone);

					if (listInfo == null) {
						System.out.println("没有找到此医生====" + str);

						addUser(name, phone);

					} else {
						System.out.println("找到此医生==" + listInfo);

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, 0, "");
	}

	@ResponseBody
	@RequestMapping("updateInvitePhone")
	public String updateUserInvitePhone(
			@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		User user = JSONObject.parseObject(data, User.class);
		try {
			mUserService.updateInvitePhone(user);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.LOGIN, result,
				JSONObject.toJSONString(user));
	}

}
