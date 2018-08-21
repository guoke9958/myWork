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
@RequestMapping("/api/search/")
public class UserControllerSearch {

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
					String mainUrl = "http://xian.liebiao.com/sou/广告/";
					try {
						for (int i = 0; i < 21; i++) {
							String url = "";
							if (i == 0) {
								url = mainUrl + "";

							} else {
								url = mainUrl + "?pn=" + i;
							}

							System.out.println(url);

							Document doc = Jsoup.connect(url).get();

							final Element el1 = doc.select("div.lmc-list").first();
							
							Elements els = el1.select("li");

							for (org.jsoup.nodes.Element el : els) {
								Element ela = el.select("a").first();
								sleep(1000);
								getChildHtmlParse(ela);
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
			System.out.println(url);
			Document doc = Jsoup.connect(url).get();

			String name = getTrueName(doc);
			String phoneNum = getPhoneNumber(doc);
			String userPhoto = getUserPhoto(doc);

			String intro = getIntro(doc);

			System.out.println(name + "\n" + phoneNum + "\n" + userPhoto);

			if (StringUtils.isEmpty(name)) {
				name = "匿名用户";
			}

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
					userInfo.setDepartmentId(333);
					userInfo.setIntro(intro);

					mUserInfoService.insertUserInfo(userInfo);

					System.out.println("添加成功");

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getIntro(Document doc) {
		try {

			Element elsTitle = doc.select("div.xiangqing").first();

			return elsTitle.html();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getAddress(Document doc) {
		try {

			Element elsTitle = doc.select("span.c").first();

			if (elsTitle != null) {
				return elsTitle.text();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getUserPhoto(Document doc1) {
		try {

			Element elsTitle = doc1.select("ul.cf").first();

			if (elsTitle != null) {
				Element elsLogo = elsTitle.select("img").first();
				return elsLogo.attr("src");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private String getPhoneNumber(Document doc) {

		try {

			Element elsTitle = doc.getElementById("btn-pn");

			if (elsTitle != null) {
				String title = elsTitle.attr("data-phone");
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

			if (elsTitle != null) {
				return elsTitle.text();
			}

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
