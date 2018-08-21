package com.xa.qyw.controller;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.User;
import com.xa.qyw.entiy.UserCapital;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.otherweb.rongyun.RongYunUtils;
import com.xa.qyw.service.MsgService;
import com.xa.qyw.service.UserCapitalService;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.service.UserService;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.MD5Util;
import com.xa.qyw.utils.StringUtils;

@Controller
@RequestMapping("/api/user58recycle/")
public class User58ControllerRecycle {

	@Autowired
	UserService mUserService;

	@Autowired
	UserCapitalService mUserCapitalService;

	@Autowired
	MsgService mMsgService;

	@Autowired
	UserInfoService mUserInfoService;

	@ResponseBody
	@RequestMapping("add")
	public String loginUser() {

		try {

			new Thread() {

				public void run() {
					String mainUrl = "http://xa.58.com/huishou/";
					try {
						for (int i = 1; i < 14; i++) {
							String url = "";
							if (i == 1) {
								url = mainUrl
										+ "?PGTID=0d300024-001e-3546-9405-bd19c569a06d&ClickID=1";

							} else {
								url = mainUrl
										+ "pn"
										+ i
										+ "/"
										+ "?PGTID=0d300024-001e-3546-9405-bd19c569a06d&ClickID=1";
							}

							System.out.println(url);

							Document doc = Jsoup.connect(url).get();

							final Elements els = doc.select("a.t");

							for (org.jsoup.nodes.Element el : els) {
								sleep(5000);
								getChildHtmlParse(el);
							}

						}
						System.out.println("添加完成");
					} catch (Exception e) {
						e.printStackTrace();
					}
				};

			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success";

	}

	private void getChildHtmlParse(Element el) {
		try {
			String linkHref = el.attr("href");
			getConnect(linkHref);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getConnect(String url) {
		try {
			Document doc = Jsoup.connect(url).get();

			System.out.println(url);

			// String title = getTitle(doc);
			// String imageList = getImageList(doc);
			String name = getTrueName(doc);
			String phoneNum = getPhoneNumber(doc);
			String userPhoto = "";

			System.out.println(name + "\n" + phoneNum + "\n" + userPhoto);
			
			if(StringUtils.isEmpty(name)){
				name = "匿名";
			}
			

			if (StringUtils.isTelActive(phoneNum)
					&& !phoneNum.contains("*")) {

				User user = new User();
				user.setPassword(MD5Util.encryptMD5("123456"));
				user.setType(6);
				user.setUserName(phoneNum);

				SimpUser simUser = mUserService.getUserByUserName(phoneNum);

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
					userInfo.setPhone(phoneNum);
					userInfo.setProvince("陕西省");
					userInfo.setCity("西安市");
					userInfo.setDepartmentId(313);

					mUserInfoService.insertUserInfo(userInfo);

					System.out.println("添加成功");

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUserPhoto(Document doc) {
		try {

			Element elsTitle = doc.select("span.cont_img").first();

			if(elsTitle!=null){
				Element img = elsTitle.select("img").first();

				if(img!=null){
					String userPhoto = img.attr("src");
					return userPhoto;
					
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getPhoneNumber(Document doc) {

		try {

			Element elsTitle = doc.getElementById("t_phone");
			if (elsTitle != null) {
				String title = elsTitle.text();
				return title;
			}
				

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String getPhoneNumber(String numer) {
		String regMobile = "(1\\d{10})";

		Pattern p1 = Pattern.compile(regMobile);

		Matcher m1 = p1.matcher(numer);
		while (m1.find()) {
			for (int i = 0; i < m1.groupCount(); i++)
				return m1.group(i + 1);
		}
		return "";
	}

	private String getTrueName(Document doc) {

		try {
			Element elsTitle = doc.select("div.su_con").first();

			Element elPhone = elsTitle.select("a").first();
			
			return elPhone.text();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getTitle(Document doc) {

		try {

			Element elsTitle = doc.select("h1.c_333").first();

			String title = elsTitle.text();
			return title;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getImageList(Document doc) {

		JSONArray jsonArr = new JSONArray();
		try {

			Element elsImg = doc.select("ul.house-pic-list").first();
			Elements imgs = elsImg.select("img");

			for (Element e : imgs) {
				String imgUrl = e.attr("lazy_src");

				jsonArr.add(imgUrl);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonArr.toJSONString();
	}

}
