package com.xa.qyw.entiy;

import java.sql.Timestamp;

public class App {

	private Integer id;
	private String versionName;
	private Integer versionCode;
	private Integer isFlag;
	private String type;
	private String updateContext;
	private Timestamp createTime;
	private String downdloadUrl;
	
	public App() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public Integer getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(Integer isFlag) {
		this.isFlag = isFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdateContext() {
		return updateContext;
	}

	public void setUpdateContext(String updateContext) {
		this.updateContext = updateContext;
	}

	public String getDowndloadUrl() {
		return downdloadUrl;
	}

	public void setDowndloadUrl(String downdloadUrl) {
		this.downdloadUrl = downdloadUrl;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
