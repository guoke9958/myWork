package com.xa.qyw.controller;

import java.util.UUID;

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
@RequestMapping("/api/user58banjia/")
public class User58ControllerBanjia {

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
					String mainUrl = "http://xa.58.com/huochec/";
					try {
						for (int i = 1; i < 12; i++) {
							String url = "";
							if (i == 1) {
								url = mainUrl
										+ "?PGTID=0d100000-001e-3c68-030d-e7d999e21d46&ClickID=1";

							} else {
								url = mainUrl
										+ "pn"
										+ i
										+ "/"
										+ "?PGTID=0d100000-001e-3c68-030d-e7d999e21d46&ClickID=1";
							}

							System.out.println(url);

							Document doc = Jsoup.connect(url).get();

							final Elements els = doc.select("td.img");

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

			Element e = el.select("a").first();
			String linkHref = e.attr("href");
			getConnect(linkHref);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getConnect(String url) {
		try {
			System.out.println(url);
			Document doc = Jsoup.connect(url).get();

			// String title = getTitle(doc);
			// String imageList = getImageList(doc);

			Element elsTitle = doc.select("script").first();

			String str = elsTitle.html();

			String name = getTrueName(doc);
			String phoneNum = getPhoneNumber(str);

			String userPhoto = getUserPhoto(doc);

			// String name = getTrueName(doc);
			// String phoneNum = getPhoneNumber(doc);
			// String userPhoto = getUserPhoto(doc);
			//
			System.out.println(name + "\n" + phoneNum + "\n" + userPhoto);

			if (StringUtils.isTelActive(phoneNum) && !phoneNum.contains("*")) {

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
					userInfo.setDepartmentId(310);

					mUserInfoService.insertUserInfo(userInfo);

					System.out.println("添加成功");

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getPhoneNumber(String str) {
		String phone = "";
		try {
			if (str.contains("phone")) {
				int startIndex = str.indexOf("phone") + 8;
				int endIndex = startIndex + 11;
				phone = str.substring(startIndex, endIndex);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return phone;
	}

	private String getUserPhoto(Document doc1) {
		try {

			Element elsTitle = doc1.select("a.dianpu").first();
			
			if(elsTitle!=null){
				String url = elsTitle.attr("href");

				Document doc = Jsoup.connect("http:" + url).get();

				Element elsLogo = doc.select("a.logo").first();

				Element elsImage = elsLogo.select("img").first();

				return elsImage.attr("src");
			}else{
				Element els = doc1.select("img.smallimg").first();
				return els.attr("src");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getPhoneNumber(Document doc) {

		try {

			Element elsTitle = doc.select("em.phone-num").first();

			if (elsTitle != null) {
				String title = elsTitle.text();

				return title;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getTrueName(Document doc) {

		try {
			Element elsTitle = doc.select("span.name").first();

			Element nameEls = elsTitle.select("a").first();

			return nameEls.text();
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
