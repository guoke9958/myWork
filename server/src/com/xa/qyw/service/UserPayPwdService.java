package com.xa.qyw.service;

import com.xa.qyw.entiy.UserPayPwd;

public interface UserPayPwdService {
	
	public int addPayPwd(UserPayPwd pwd);
	
	public int updatePayPwd(UserPayPwd pwd);
	
	public UserPayPwd checkUserPayPwd(UserPayPwd pwd);
	
	public UserPayPwd getUserPayPwdByUserId(String userId);
	
	public int updatePayErrorCount(UserPayPwd pwd);

}
