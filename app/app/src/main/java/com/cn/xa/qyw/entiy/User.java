package com.cn.xa.qyw.entiy;

import java.io.Serializable;

public class User implements Serializable{
	
	private int id;
	private String userId;
	private String userName;
	private int type;
	private String token;
	private String password;      //密码
	private String requestType;   //注册类型
	private String invitePhone;   // 邀请人手机号
	
	public User() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getInvitePhone() {
		return invitePhone;
	}

	public void setInvitePhone(String invitePhone) {
		this.invitePhone = invitePhone;
	}
}
