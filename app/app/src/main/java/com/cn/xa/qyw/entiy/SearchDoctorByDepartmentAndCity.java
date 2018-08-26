package com.cn.xa.qyw.entiy;

public class SearchDoctorByDepartmentAndCity {
	
	private int departmentId;
	private int hospitalId;
	private String grade;
<<<<<<< HEAD
	private String mGradeId;
=======
	private int id;
>>>>>>> amoldzhang
	private String city;
	
	public SearchDoctorByDepartmentAndCity() {
		super();
	}

<<<<<<< HEAD
	public String getmGradeId() {
		return mGradeId;
	}

	public void setmGradeId(String mGradeId) {
		this.mGradeId = mGradeId;
=======
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
>>>>>>> amoldzhang
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
