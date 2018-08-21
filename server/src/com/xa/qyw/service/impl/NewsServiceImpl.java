package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.NewsDao;
import com.xa.qyw.entiy.News;
import com.xa.qyw.entiy.NewsDetail;
import com.xa.qyw.service.NewsService;

@Service("newsService")
public class NewsServiceImpl implements NewsService{

	
	
	@Resource(name = "newsDao")
	private NewsDao mNewsDao;
	
	@Override
	public News queryNews(String id) {
		return mNewsDao.queryNews(id);
	}

	@Override
	public void addNews(News news) {
		mNewsDao.addNews(news);
	}

	@Override
	public void updateNews(News news) {
		mNewsDao.updateNews(news);
	}

	@Override
	public List<News> getNewsByPage(News news) {
		return mNewsDao.getNewsByPage(news);
	}

	@Override
	public int addNewsDetail(NewsDetail detail) {
		return mNewsDao.addNewsDetail(detail);
	}

	@Override
	public NewsDetail queryNewsDetail(String id) {
		return mNewsDao.queryNewsDetail(id);
	}

	@Override
	public List<NewsDetail> getNewsDetailByPage() {
		return mNewsDao.getNewsDetailByPage();
	}

	@Override
	public void addNewsDetailBrowseCount(NewsDetail detail) {
		mNewsDao.addNewsDetailBrowseCount(detail);
	}

	@Override
	public News getMaxNews() {
		return mNewsDao.getMaxNews();
	}

}
