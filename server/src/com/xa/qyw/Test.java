package com.xa.qyw;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Test {

	public static void main(String[] args) {

		String url = "http://xa.58.com/chuzu/?utm_source=market&spm=b-31580022738699-me-f-824.bdpz_biaoti&PGTID=0d100000-001e-3ec4-5b9e-7f5afa87d445&ClickID=1";
		try {
			Document doc = Jsoup.connect(url).get();
			List<Element> els = doc.select("div.des");
			List<String> lists = new ArrayList<String>();
			for (Element el : els) {
				Element link = el.select("h2 > a").first();
				String ur = link.attr("href");
				lists.add(ur);
			}

			addUserInfo(lists);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void addUserInfo(List<String> lists) {

		for (String url : lists) {
			try {

				Document doc = Jsoup.connect(url).get();
				Element el = doc.select("h1.c_333").first();
				Element elName = doc.select("a.c_000").first();
				Element elPhone = doc.select("em.phone-num").first();
				if (el != null && elName != null&&elPhone!=null) {
					String title = el.text();
					String name = elName.text();
					String phone = elPhone.text();
					System.out.println(title + "======" + name+"===="+phone);
				} else {
					System.out.println("获取信息失败");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
