package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.UserAliAccount;

public interface UserAliAccountDao {

	public List<UserAliAccount> getUserAliAccountByUserId(String userId);
	
	public void deleteUserAliAccount(int id);
	
	public void addUserAliAccount(UserAliAccount aliAccount);
	
	public UserAliAccount queryUserAliAccount(UserAliAccount aliAccount);
}
