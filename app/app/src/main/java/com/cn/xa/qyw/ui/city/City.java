package com.cn.xa.qyw.ui.city;

import java.io.Serializable;

public class City implements Serializable{
	public String name;
	private String province;
	public String pinyi;

	public City(String province,String name, String pinyi) {
		super();
		this.name = name;
		this.pinyi = pinyi;
		this.province = province;
	}

	public City() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}
