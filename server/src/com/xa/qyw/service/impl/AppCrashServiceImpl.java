package com.xa.qyw.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.AppCrashDao;
import com.xa.qyw.entiy.AppCrash;
import com.xa.qyw.service.AppCrashService;


@Service("appCrashService")
public class AppCrashServiceImpl implements AppCrashService {

	@Resource(name = "appCrashDao")
	private AppCrashDao appCrashDao;

	@Override
	public void insertAppCrash(AppCrash crash) {
		appCrashDao.insertAppCrash(crash);		
	}

	
}
