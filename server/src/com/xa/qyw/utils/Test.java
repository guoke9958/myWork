package com.xa.qyw.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.JDOMException;

import com.alibaba.fastjson.JSONObject;

public class Test {
	private static final String regex = " <td.*?>\\s*<p.*?>\\s*<a.*?>.*?</a>\\s*</p>\\s*<p.*?>\\s*<a.*?>(.*?)</a>\\s*<br>\\s*<font.*?>.*?</p>\\s*<p.*?>\\s*</td>";

	public static void main(String[] args) {
		try {
			HttpURLConnection urlconn = (HttpURLConnection) new URL(
					"http://yyk.99.com.cn/weinan/").openConnection();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					urlconn.getInputStream()));

			String temp = null;
			StringBuffer sb = new StringBuffer();
			temp = rd.readLine();
			while (temp != null) {
				sb.append(temp);
				temp = rd.readLine();
			}
			rd.close();
			urlconn.disconnect();

			String content = sb.toString();

			System.out.println(content);
			
//
//			Pattern p = Pattern.compile(regex);
//			Matcher ma = p.matcher(content);
//
//			System.out.println("强档专区的电影：");
//			while (ma.find()) {
//				System.out.println(ma.group(1));
//			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

