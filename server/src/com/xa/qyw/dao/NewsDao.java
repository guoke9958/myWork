package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.News;
import com.xa.qyw.entiy.NewsDetail;

public interface NewsDao {
	
	public News queryNews(String id);
	
	public void addNews(News news);
	
	public void updateNews(News news);
	
	public List<News> getNewsByPage(News news);
	
	public int addNewsDetail(NewsDetail detail);
	
	public NewsDetail queryNewsDetail(String id);
	
	public List<NewsDetail> getNewsDetailByPage();
	
	public void addNewsDetailBrowseCount(NewsDetail detail);
	
	public News getMaxNews();

}
