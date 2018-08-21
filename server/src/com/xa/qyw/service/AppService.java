package com.xa.qyw.service;

import java.util.List;

import com.xa.qyw.entiy.App;

public interface AppService {

	/**
	 * �ϴ�app
	 */
	public void addApp(App app);
	
	/**
	 * ɾ��App
	 */
	public void deleteApp(App app);

	/**
	 * ��ѯ���汾
	 */
	public App getMaxApp(int code);
	
	/**
	 * ��ѯ���а汾��Ϣ
	 */
	public List<App> getAllApp();
	
	/**
	 * ��ѯ����versionCode
	 */
	public int getMaxAppVersionCode();
	
}
