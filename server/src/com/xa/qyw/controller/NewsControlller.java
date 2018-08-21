package com.xa.qyw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.News;
import com.xa.qyw.entiy.NewsDetail;
import com.xa.qyw.service.NewsService;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.StringUtils;

@Controller
@RequestMapping("/api/news/")
public class NewsControlller {

	@Autowired
	NewsService mNewsService;

	@RequestMapping(value = "/add_news_view")
	public String getMaxAppJsp(HttpServletRequest request, Model model) {
		News news = mNewsService.getMaxNews();

		if(news.getIsComplete() != 1){
			model.addAttribute("id", news.getId());
		}else {
			model.addAttribute("id", news.getId() + 1);
		}

		return "/add_news_detail";
	}

	@RequestMapping(value = "/news_detail_view")
	public String newsDetail(HttpServletRequest request, Model model) {
		NewsDetail list = mNewsService.queryNewsDetail(request
				.getParameter("id"));
		model.addAttribute("newsDetail", list.getContent());
		return "/news_detail";
	}

	@RequestMapping("add")
	public String addNews(HttpServletRequest request, Model model) {

		String des = "添加失败";

		try {
			News news = mNewsService.queryNews(request.getParameter("id"));
			String isComplete = request.getParameter("is_complete");
			if (news == null) {
				System.out.println("不存在，开始添加");

				news = new News();
				news.setId(0);
				news.setCreateTime(DateUtils.getCurrentTimestamp());
				news.setIsComplete(Integer.parseInt(isComplete));
				news.setNewsFirst(addNewsDetail(request).toString());
				mNewsService.addNews(news);
				des = "添加成功";
			} else {
				System.out.println("已存在，进行更新");

				if (StringUtils.isEmpty(news.getNewsFirst())
						|| StringUtils.isEmpty(news.getNewsFirst())
						|| StringUtils.isEmpty(news.getNewsThree())) {
					String json = addNewsDetail(request).toString();
					des = "更新成功";
					news.setIsComplete(Integer.parseInt(isComplete));
					if (StringUtils.isEmpty(news.getNewsFirst())) {
						news.setNewsFirst(json);
						mNewsService.updateNews(news);
					} else if (StringUtils.isEmpty(news.getNewsSecond())) {
						news.setNewsSecond(json);
						mNewsService.updateNews(news);
					} else if (StringUtils.isEmpty(news.getNewsThree())) {
						news.setNewsThree(json);
						mNewsService.updateNews(news);
					} else {
						des = "更新失败";
					}
				} else {
					des = "更新失败";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

			des = "更新失败";
		}

		News news = mNewsService.getMaxNews();

		if (StringUtils.isEmpty(news.getNewsFirst())
				|| StringUtils.isEmpty(news.getNewsSecond())
				|| StringUtils.isEmpty(news.getNewsThree())) {
			model.addAttribute("id", news.getId());
		} else {
			model.addAttribute("id", news.getId() + 1);
		}

		model.addAttribute("result", des);
		return "/add_news_detail";
	}

	private JSONObject addNewsDetail(HttpServletRequest request) {
		NewsDetail detail = new NewsDetail();
		detail.setAuthor(request.getParameter("author"));
		detail.setTitle(request.getParameter("title"));
		detail.setBrowseCount(0);
		detail.setNewsPhoto(request.getParameter("image"));
		detail.setSource(request.getParameter("source"));
		detail.setNewsType(1);
		String httpAddress = request.getParameter("http_address");

		if (StringUtils.isEmpty(httpAddress)) {
			detail.setContent(request.getParameter("content"));
			detail.setIsReprint(0);
		} else {
			detail.setContent(request.getParameter("http_address"));
			detail.setIsReprint(1);
		}

		mNewsService.addNewsDetail(detail);

		System.out.println("添加的新闻====" + detail.getNewsId());

		JSONObject json = new JSONObject();
		json.put("id", detail.getNewsId());
		json.put("title", detail.getTitle());
		json.put("url", detail.getNewsPhoto());
		return json;
	}

	@ResponseBody
	@RequestMapping("query_news")
	public String getNewsDetailByPage() {
		int result = ResponseUtils.FAIL;
		List<NewsDetail> list = new ArrayList<NewsDetail>();
		try {

			list = mNewsService.getNewsDetailByPage();
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result,
				JSONObject.toJSONString(list));
	}

	@ResponseBody
	@RequestMapping("get_news")
	public String getNewsByPage(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<News> list = new ArrayList<News>();
		try {

			list = mNewsService.getNewsByPage(JSONObject.parseObject(data,
					News.class));
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result,
				JSONObject.toJSONString(list));
	}

	@ResponseBody
	@RequestMapping("news_detail")
	public String getNewsDetailById(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		NewsDetail list = null;
		try {

			list = mNewsService.queryNewsDetail(data);
			list.setBrowseCount(list.getBrowseCount() + 1);
			mNewsService.addNewsDetailBrowseCount(list);

			if (list.getIsReprint() != 1) {
				list.setContent("");
			}

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result,
				JSONObject.toJSONString(list));
	}

}
