package com.xa.qyw.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.UserPayPwdDao;
import com.xa.qyw.entiy.UserPayPwd;
import com.xa.qyw.service.UserPayPwdService;

@Service("userPayPwdService")
public class UserPayPwdServiceImpl implements UserPayPwdService {
	
	@Resource(name = "userPayPwdDao")
	private UserPayPwdDao mUserPayPwdDao;

	@Override
	public int addPayPwd(UserPayPwd pwd) {
		return mUserPayPwdDao.addPayPwd(pwd);
	}

	@Override
	public int updatePayPwd(UserPayPwd pwd) {
		return mUserPayPwdDao.updatePayPwd(pwd);
	}

	@Override
	public UserPayPwd checkUserPayPwd(UserPayPwd pwd) {
		return mUserPayPwdDao.checkUserPayPwd(pwd);
	}

	@Override
	public UserPayPwd getUserPayPwdByUserId(String userId) {
		return mUserPayPwdDao.getUserPayPwdByUserId(userId);
	}

	@Override
	public int updatePayErrorCount(UserPayPwd pwd) {
		return mUserPayPwdDao.updatePayErrorCount(pwd);
	}

}
