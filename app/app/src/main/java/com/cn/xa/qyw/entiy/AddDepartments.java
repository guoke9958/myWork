package com.cn.xa.qyw.entiy;

public class AddDepartments {
	
	private int id;
	private String departmentsName;
	
	public AddDepartments() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDepartmentsName() {
		return departmentsName;
	}

	public void setDepartmentsName(String departmentsName) {
		this.departmentsName = departmentsName;
	}

	@Override
	public String toString() {
		return departmentsName;
	}
}
