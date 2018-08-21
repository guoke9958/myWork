package com.xa.qyw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.entiy.SearchHot;
import com.xa.qyw.entiy.SearchItem;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.service.SearchService;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/search/")
public class SearchController {

	@Autowired
	UserInfoService mUserInfoService;
	
	@Autowired
	SearchService mSearchService;
	
	
	@ResponseBody
	@RequestMapping("key")
	public String searchDoctor(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
			SearchItem item = JSONObject.parseObject(data,SearchItem.class);
			list = mUserInfoService.searchDoctor(item);
			
			if(item.isFirst()){
				item.setKey(item.getKey().replace("%", ""));
				mSearchService.addSearchHistory(item);
			}
			
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(1000, result,
				JSONObject.toJSONString(list));
	}
	
	@ResponseBody
	@RequestMapping("hot")
	public String getAllSearchHot() {
		int result = ResponseUtils.FAIL;
		List<SearchHot> list = new ArrayList<SearchHot>();
		try {
			list = mSearchService.getAllSearchHot();
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(1000, result,
				JSONObject.toJSONString(list));
	}
	
}
