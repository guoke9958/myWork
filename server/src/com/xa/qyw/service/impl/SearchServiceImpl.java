package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.SearchDao;
import com.xa.qyw.entiy.SearchHot;
import com.xa.qyw.entiy.SearchItem;
import com.xa.qyw.service.SearchService;

@Service("searchService")
public class SearchServiceImpl implements SearchService{

	@Resource(name = "searchDao")
	private SearchDao mSearchDao;
	
	@Override
	public List<SearchHot> getAllSearchHot() {
		return mSearchDao.getAllSearchHot();
	}

	@Override
	public void addSearchHistory(SearchItem item) {
		mSearchDao.addSearchHistory(item);
	}

}
