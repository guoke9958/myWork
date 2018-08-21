package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.UserInfoDao;
import com.xa.qyw.entiy.SearchItem;
import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.service.UserInfoService;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	
	
	@Resource(name = "userInfoDao")
	private UserInfoDao mUserInfoDao;

	@Override
	public void updateUserId(SimpUser user) {
		mUserInfoDao.updateUserId(user);
	}

	@Override
	public UserInfo getUserInfoById(String userId) {
		return mUserInfoDao.getUserInfoById(userId);
	}

	@Override
	public void updateUserInfoPhoto(UserInfo info) {
		mUserInfoDao.updateUserInfoPhoto(info);
	}

	@Override
	public void updateUserInfoAll(UserInfo info) {
		mUserInfoDao.updateUserInfoAll(info);
	}

	@Override
	public List<UserInfo> getAllUserInfo() {
		return mUserInfoDao.getAllUserInfo();
	}

	@Override
	public void updateUserName(UserInfo info) {
		mUserInfoDao.updateUserName(info);
	}

	@Override
	public void insertUserInfo(UserInfo info) {
		mUserInfoDao.insertUserInfo(info);
	}

	@Override
	public List<UserInfo> queryUserInfoByName(String name) {
		return mUserInfoDao.queryUserInfoByName(name);
	}

	@Override
	public void updateUserPhone(UserInfo info) {
		mUserInfoDao.updateUserPhone(info);
	}

	@Override
	public void deleteUserInfo(String userId) {
		mUserInfoDao.deleteUserInfo(userId);
	}

	@Override
	public void updateUserInfoHospital(UserInfo info) {
		mUserInfoDao.updateUserInfoHospital(info);
		
	}

	@Override
	public List<UserInfo> searchDoctor(SearchItem item) {
		return mUserInfoDao.searchDoctor(item);
	}

}
