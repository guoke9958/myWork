package com.xa.qyw.entiy;

import java.sql.Timestamp;

public class Voucher {
	
	private int id;
	private String userId;
	private int userVoucher;
	private Timestamp createTime;
	
	public Voucher() {
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getUserVoucher() {
		return userVoucher;
	}

	public void setUserVoucher(int userVoucher) {
		this.userVoucher = userVoucher;
	}
	
	

}
