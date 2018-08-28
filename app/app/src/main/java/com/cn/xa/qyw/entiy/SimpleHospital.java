package com.cn.xa.qyw.entiy;

import java.util.List;

public class SimpleHospital {
	
	private int hospitalId;
	private String hospitalName;
	private String hospitalLogo;
	private List<SimpleDepartment> departments;
	
	public SimpleHospital() {
		super();
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalLogo() {
		return hospitalLogo;
	}

	public void setHospitalLogo(String hospitalLogo) {
		this.hospitalLogo = hospitalLogo;
	}

	public List<SimpleDepartment> getDepartments() {
		return departments;
	}

	public void setDepartments(List<SimpleDepartment> departments) {
		this.departments = departments;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}
}
