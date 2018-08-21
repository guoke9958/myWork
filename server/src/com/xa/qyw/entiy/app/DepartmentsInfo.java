package com.xa.qyw.entiy.app;

public class DepartmentsInfo {
	
	private int departmentsId;
	private int hospitalId;
	private String departmentsName;
	private String hospitalName;
	private String hospitalLogo;
	
	public DepartmentsInfo() {
		super();
	}

	public int getDepartmentsId() {
		return departmentsId;
	}

	public void setDepartmentsId(int departmentsId) {
		this.departmentsId = departmentsId;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getDepartmentsName() {
		return departmentsName;
	}

	public void setDepartmentsName(String departmentsName) {
		this.departmentsName = departmentsName;
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
	
}
