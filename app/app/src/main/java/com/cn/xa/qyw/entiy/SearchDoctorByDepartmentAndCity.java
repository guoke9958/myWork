package com.cn.xa.qyw.entiy;

public class SearchDoctorByDepartmentAndCity {
	
	private int departmentId;
	private int hospitalId;
	private String grade;
	private String mGradeId;
	private String city;
	
	public SearchDoctorByDepartmentAndCity() {
		super();
	}

	public String getmGradeId() {
		return mGradeId;
	}

	public void setmGradeId(String mGradeId) {
		this.mGradeId = mGradeId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
}
