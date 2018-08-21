package com.xa.qyw.entiy;

import java.sql.Timestamp;

public class UserAliAccount {

	private int id;
	private String userId;
	private String aliName;
	private String aliAccount;
	private Timestamp createTime;
	
	public UserAliAccount() {
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

	public String getAliName() {
		return aliName;
	}

	public void setAliName(String aliName) {
		this.aliName = aliName;
	}

	public String getAliAccount() {
		return aliAccount;
	}

	public void setAliAccount(String aliAccount) {
		this.aliAccount = aliAccount;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
	
}
