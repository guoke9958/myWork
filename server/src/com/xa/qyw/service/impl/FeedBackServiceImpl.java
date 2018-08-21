package com.xa.qyw.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.FeedBackDao;
import com.xa.qyw.entiy.FeedBack;
import com.xa.qyw.service.FeedBackService;


@Service("feedBackService")
public class FeedBackServiceImpl implements FeedBackService {

	@Resource(name = "feedBackDao")
	private FeedBackDao feedBackDao;

	@Override
	public int addFeedBack(FeedBack feedback) {
		return feedBackDao.addFeedBack(feedback);
	}


	
}
