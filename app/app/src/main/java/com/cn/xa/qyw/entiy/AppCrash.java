package com.cn.xa.qyw.entiy;

import java.sql.Timestamp;

public class AppCrash {
	private int id;
	private String userId;
	private String deviceInfo;
	private String content;
	private int isSolve;
	private Timestamp createTime;
	
	public AppCrash() {
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

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getIsSolve() {
		return isSolve;
	}

	public void setIsSolve(int isSolve) {
		this.isSolve = isSolve;
	}
	
	

}
