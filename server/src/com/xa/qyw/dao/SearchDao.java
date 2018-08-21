package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.SearchHot;
import com.xa.qyw.entiy.SearchItem;

public interface SearchDao {
	
	public List<SearchHot> getAllSearchHot();
	
	public void addSearchHistory(SearchItem item);
}
