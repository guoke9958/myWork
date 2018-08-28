package com.cn.xa.qyw.entiy;

import java.sql.Timestamp;

public class Order {
	
	private int id;
	private String orderId;
	private Timestamp updateTime;
	
	public Order() {
		super();
	}

	public Order(int id, String orderId, Timestamp updateTime) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.updateTime = updateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
