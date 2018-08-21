package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.UserAliAccountDao;
import com.xa.qyw.entiy.UserAliAccount;
import com.xa.qyw.service.UserAliAccountService;

@Service("userAliAccountService")
public class UserAliAccountServiceImpl implements UserAliAccountService{

	@Resource(name = "userAliAccountDao")
	private UserAliAccountDao mUserAliAccountDao;
	
	@Override
	public List<UserAliAccount> getUserAliAccountByUserId(String userId) {
		return mUserAliAccountDao.getUserAliAccountByUserId(userId);
	}

	@Override
	public void deleteUserAliAccount(int id) {
		mUserAliAccountDao.deleteUserAliAccount(id);
	}

	@Override
	public void addUserAliAccount(UserAliAccount aliAccount) {
		mUserAliAccountDao.addUserAliAccount(aliAccount);
	}

	@Override
	public UserAliAccount queryUserAliAccount(UserAliAccount aliAccount) {
		return mUserAliAccountDao.queryUserAliAccount(aliAccount);
	}

}
