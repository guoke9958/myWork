package com.xa.qyw.entiy;

import java.sql.Timestamp;

public class CapitalHistory {
	private int id;
	private String orderId;
	private String userId;
	private String userName;
	private String userPhoto;
	private int capitalType;
	private String toUserId;
	private String payType;
	private Timestamp updateTime;
	private String toUserName;
	private String toUserPhoto;
	private int change;
	private int toUserType;
	
	public CapitalHistory() {
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

	public int getCapitalType() {
		return capitalType;
	}

	public void setCapitalType(int capitalType) {
		this.capitalType = capitalType;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public int getChange() {
		return change;
	}

	public void setChange(int change) {
		this.change = change;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public int getToUserType() {
		return toUserType;
	}

	public void setToUserType(int toUserType) {
		this.toUserType = toUserType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getToUserPhoto() {
		return toUserPhoto;
	}

	public void setToUserPhoto(String toUserPhoto) {
		this.toUserPhoto = toUserPhoto;
	}
	
	

}
