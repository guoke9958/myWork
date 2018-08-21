package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.SearchItem;
import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.UserInfo;

public interface UserInfoDao {
	
	public void updateUserId(SimpUser user);
	
	public UserInfo getUserInfoById(String userId);
	
	public void updateUserInfoPhoto(UserInfo info);
	
	public void updateUserInfoAll(UserInfo info);
	
	public List<UserInfo> getAllUserInfo();
	
	public void updateUserName(UserInfo info);
	
	public void insertUserInfo(UserInfo info);
	
	public List<UserInfo> queryUserInfoByName(String name);
	
	public void updateUserPhone(UserInfo info);
	
	public void deleteUserInfo(String userId);
	
	public void updateUserInfoHospital(UserInfo info);
	
	public List<UserInfo> searchDoctor(SearchItem item);
	
}
