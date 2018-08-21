package com.cn.xa.qyw.entiy;

import java.io.Serializable;

public class SimpleDoctor implements Serializable{
	private int id;
	private String doctorId;
	private String doctorName;
	private String nickName;
	private String doctorPhoto;
	private String dcotorProfessionalTitle;
	private int hospitalId;
	private String hospitalName;
	
	public SimpleDoctor() {
		super();
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorPhoto() {
		return doctorPhoto;
	}

	public void setDoctorPhoto(String doctorPhoto) {
		this.doctorPhoto = doctorPhoto;
	}

	public String getDcotorProfessionalTitle() {
		return dcotorProfessionalTitle;
	}

	public void setDcotorProfessionalTitle(String dcotorProfessionalTitle) {
		this.dcotorProfessionalTitle = dcotorProfessionalTitle;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	

}
