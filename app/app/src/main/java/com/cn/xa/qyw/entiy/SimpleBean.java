package com.cn.xa.qyw.entiy;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class SimpleBean implements Serializable{
	
	
	private Integer bizCode;
	private Integer result;
	private String data;
	private String description;
	
	public SimpleBean() {
		super();
	}

	public SimpleBean(Integer bizCode, Integer result, String data) {
		super();
		this.bizCode = bizCode;
		this.result = result;
		this.data = data;
	}
	
	

	public SimpleBean(Integer bizCode, Integer result, String data,
			String description) {
		super();
		this.bizCode = bizCode;
		this.result = result;
		this.data = data;
		this.description = description;
	}

	public Integer getBizCode() {
		return bizCode;
	}

	public void setBizCode(Integer bizCode) {
		this.bizCode = bizCode;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
