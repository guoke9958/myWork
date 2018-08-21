package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.AppDao;
import com.xa.qyw.entiy.App;
import com.xa.qyw.service.AppService;


@Service("appService")
public class AppServiceImpl implements AppService {

	@Resource(name = "appDao")
	private AppDao appDao;

	@Override
	public void addApp(App app) {
		appDao.addApp(app);
	}

	@Override
	public void deleteApp(App app) {
		appDao.deleteApp(app);
	}

	@Override
	public App getMaxApp(int code) {
		return appDao.getMaxApp(code);
	}

	@Override
	public List<App> getAllApp() {
		return appDao.getAllApp();
	}

	@Override
	public int getMaxAppVersionCode() {
		return appDao.getMaxAppVersionCode();
	}

}
