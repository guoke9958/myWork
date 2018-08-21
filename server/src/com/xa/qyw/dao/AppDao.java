package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.App;

public interface AppDao {
	
	/**
	 * 上传app
	 */
	public void addApp(App app);
	
	/**
	 * 删除App
	 */
	public void deleteApp(App app);

	/**
	 * 查询最大版本
	 */
	public App getMaxApp(int code);
	
	/**
	 * 查询所有版本信息
	 */
	public List<App> getAllApp();
	
	/**
	 * 查询最大的versionCode
	 */
	public int getMaxAppVersionCode();
}
