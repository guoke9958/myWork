package com.xa.qyw.otherweb.note;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.xa.qyw.utils.StringUtils;

public class MsgSend {

	public static void main(String[] args) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();

		String filePath = "D:/专家号码.txt";

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferedreader = new BufferedReader(fr);
			String instring;
			while ((instring = bufferedreader.readLine()) != null) {
				if (0 != instring.length()) {
					sb.append(instring);
					sb.append(",");
				}
			}
			fr.close();

			sb.deleteCharAt(sb.toString().length() - 1);

			System.out.println(sb.toString());
		} catch (Exception e) {
		}

		long code = 0;
		try {
			String userName = "clykwcqyw";
			String password = "rgqfwesq";

			String content = "尊敬的各位专家老师们，你们好！我是飞腾公司杨志尚。历经千辛万苦，求医网医患通APP终于上线了，各位的资料也在APP上展示了.敬请下载该APP登录，用户名是自己姓名的汉语全拼，密码是123456。咱们的APP就像微信那样，医患之间可直接聊天。咨询完毕，患者可给医生打赏。我们正在招聘各地服务站代理。据测算，如果一个医生能每天坚持服务患者，增加患者粉丝，仅打赏收入(医生占70%，代理10%，平台10%，税10%)，每年不低于3—5万元。患者慕名来就诊也可给科室或医院带来很大收入，利国利民，皆大欢喜。医患通公众号：qiuyiwangft 下载地址： http://qiuyiwang.com/xiaz.php再次向各位专家教授致以最诚挚的感谢! 13060384913，陕西飞腾医疗管理有限公司";

			Client client = new Client(userName, password);

			String result = client.mt(URLEncoder.encode(content, "UTF-8"),
					sb.toString(), "", "", "", "");

			String strCode = result.split("\n")[0];

			code = Long.valueOf(strCode);
			String info = MsgSend.getMsgSendState(code);

			System.out.println(info);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static long sendNoteVersionCode(String authCode, String mobile) {
		long code = 0;
		try {
			String userName = "clykwcqyw";
			String password = "rgqfwesq";
			String content = "尊敬的用户，您的短信验证码为：" + authCode + " 祝您生活愉快";

			Client client = new Client(userName, password);
			String result = client.mt(URLEncoder.encode(content, "UTF-8"),
					mobile, "", "", "", "");
			System.out.println(result);
			String strCode = result.split("\n")[0];

			code = Long.valueOf(strCode);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return code;
	}

	public static long sendNoteOrderMsg(String doctorName, String name,
			String data, String mobile, String sex, String birthday,
			String address) {
		long code = 0;
		try {
			
			String userName = "clykwcqyw";
			String password = "rgqfwesq";
			
			if(StringUtils.isEmpty(sex)){
				sex = "男";
			}
			
			int age = getAge(parse(birthday));
			String content = doctorName + "医生您好，" + name + "，" + sex + "，"
					+ age + "岁，" + address + "，向您预约看病（" + data
					+ "），请登录医患通APP：qiuyiwang.com/xiaz.php，用您的手机号登录，密码:123456,接收患者信息，谢谢！";

			Client client = new Client(userName, password);
			String result = client.mt(URLEncoder.encode(content, "UTF-8"),
					mobile, "", "", "", "");
			System.out.println(result);
			String strCode = result.split("\n")[0];

			code = Long.valueOf(strCode);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return code;
	}
	
	public static long sendNoteOrderMsg(String doctorName, String name,
			String data, String mobile, String address) {
		long code = 0;
		try {
			
			String userName = "clykwcqyw";
			String password = "rgqfwesq";
			
			String content = doctorName + "您好，" + name + "，"+"向您预约（" + data
					+ "），请用您的手机号登录并查看信息，谢谢！App下载地址： http://qiuyiwang.com/xiaz.php";

			Client client = new Client(userName, password);
			String result = client.mt(URLEncoder.encode(content, "UTF-8"),
					mobile, "", "", "", "");
			System.out.println(result);
			String strCode = result.split("\n")[0];

			code = Long.valueOf(strCode);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return code;
	}
	

	public static Date parse(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(strDate);
	}

	// 由出生日期获得年龄
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return age;
	}

	public static String getRandomCode() {
		int random = (int) (Math.random() * 9000 + 10000);
		return random + "";
	}

	public static String getMsgSendState(long code) {
		String Info = null;
		if (code > 0) {// 成功提交
			Info = "发送成功";
		} else if (code == 0) {
			Info = "发送失败";
		} else if (code == -1) { // 用户名密码不正确
			Info = "用户名密码不正确";
		} else if (code == -2) { // 必填选项为空
			Info = "必填选项为空";
		} else if (code == -3) { // 短信内容0个字节
			Info = "短信内容0个字节";
		} else if (code == -4) { // 0个有效号码
			Info = "0个有效号码";
		} else if (code == -5) { // 余额不够
			Info = "余额不够";
		} else if (code == -10) { // 用户被禁用
			Info = "用户被禁用";
		} else if (code == -11) { // 短信内容过长
			Info = "短信内容过长";
		} else if (code == -12) { // 用户无扩展权限
			Info = "无扩展权限";
		} else if (code == -13) { // IP地址校验错
			Info = "IP校验错误";
		} else if (code == -14) { // 内容解析异常
			Info = "内容解析异常";
		} else {
			Info = "未知错误";
		}
		return Info;
	}

}
