package com.cn.xa.qyw.entiy;

import java.util.List;


public class DepartmentInfo {
	private int id;
	private int hospitalId;
	private int departmentId;
	private String hospitalName;
	private String departmentName;
	private String intro;
	private String phone;
	private List<SimpleDoctor> listDoctor;
	
	
	public DepartmentInfo() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getHospitalId() {
		return hospitalId;
	}


	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}


	public int getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}


	public String getIntro() {
		return intro;
	}


	public void setIntro(String intro) {
		this.intro = intro;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getHospitalName() {
		return hospitalName;
	}


	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	public List<SimpleDoctor> getListDoctor() {
		return listDoctor;
	}


	public void setListDoctor(List<SimpleDoctor> listDoctor) {
		this.listDoctor = listDoctor;
	}
	
	

}
