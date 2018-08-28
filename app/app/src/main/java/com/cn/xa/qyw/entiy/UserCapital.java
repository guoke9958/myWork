package com.cn.xa.qyw.entiy;

import java.sql.Timestamp;

public class UserCapital {

	private int id;
	private String userId;
	private int capitalTotal;
	private int expandCapital;
	private int rechargeCapital;
	private int changeCapital;
	private int incomeCapital;
	private Timestamp updateTime;
	
	public UserCapital() {
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

	public float getCapitalTotal() {
		return capitalTotal;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public int getExpandCapital() {
		return expandCapital;
	}

	public void setExpandCapital(int expandCapital) {
		this.expandCapital = expandCapital;
	}

	public int getRechargeCapital() {
		return rechargeCapital;
	}

	public void setRechargeCapital(int rechargeCapital) {
		this.rechargeCapital = rechargeCapital;
	}

	public void setCapitalTotal(int capitalTotal) {
		this.capitalTotal = capitalTotal;
	}

	public int getChangeCapital() {
		return changeCapital;
	}

	public void setChangeCapital(int changeCapital) {
		this.changeCapital = changeCapital;
	}

	public int getIncomeCapital() {
		return incomeCapital;
	}

	public void setIncomeCapital(int incomeCapital) {
		this.incomeCapital = incomeCapital;
	}
	
	
}
