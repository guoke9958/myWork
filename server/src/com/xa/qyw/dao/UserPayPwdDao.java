package com.xa.qyw.dao;

import com.xa.qyw.entiy.UserPayPwd;

public interface UserPayPwdDao {
	
	public int addPayPwd(UserPayPwd pwd);
	
	public int updatePayPwd(UserPayPwd pwd);
	
	public UserPayPwd checkUserPayPwd(UserPayPwd pwd);
	
	public UserPayPwd getUserPayPwdByUserId(String userId);
	
	public int updatePayErrorCount(UserPayPwd pwd);

}
