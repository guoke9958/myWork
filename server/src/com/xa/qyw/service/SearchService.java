package com.xa.qyw.service;

import java.util.List;

import com.xa.qyw.entiy.SearchHot;
import com.xa.qyw.entiy.SearchItem;

public interface SearchService {

	public List<SearchHot> getAllSearchHot();
	
	public void addSearchHistory(SearchItem item);
	
}
