package com.cn.xa.qyw.entiy;

import java.io.Serializable;
import java.sql.Timestamp;

public class HospitalGrade implements Serializable {

	private int id;
	private String gradeName;
	private Timestamp createTime;
	
	public HospitalGrade() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
