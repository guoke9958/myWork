package com.xa.qyw.entiy;

import java.util.List;

import com.xa.qyw.entiy.app.SimpleDepartment;

public class HospitalDetailInfo {

	private int id;
	private String hospital_name;
	private String hospital_grade;
	private String hospital_province;
	private String hospital_city;
	private String hospital_area;
	private String hospital_address;
	private String hospital_path;
	private String hospital_phone;
	private String hospital_intro;
	private String hospital_logo;
	private String hospital_lat;
	private String hospital_lng;
	private int hospital_type;
	private int is_pass;
	private List<SimpleDepartment> listDepart;
	
	public HospitalDetailInfo() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHospital_name() {
		return hospital_name;
	}

	public void setHospital_name(String hospital_name) {
		this.hospital_name = hospital_name;
	}

	public String getHospital_grade() {
		return hospital_grade;
	}

	public void setHospital_grade(String hospital_grade) {
		this.hospital_grade = hospital_grade;
	}

	public String getHospital_province() {
		return hospital_province;
	}

	public void setHospital_province(String hospital_province) {
		this.hospital_province = hospital_province;
	}

	public String getHospital_city() {
		return hospital_city;
	}

	public void setHospital_city(String hospital_city) {
		this.hospital_city = hospital_city;
	}

	public String getHospital_address() {
		return hospital_address;
	}

	public void setHospital_address(String hospital_address) {
		this.hospital_address = hospital_address;
	}

	public String getHospital_path() {
		return hospital_path;
	}

	public void setHospital_path(String hospital_path) {
		this.hospital_path = hospital_path;
	}

	public String getHospital_phone() {
		return hospital_phone;
	}

	public void setHospital_phone(String hospital_phone) {
		this.hospital_phone = hospital_phone;
	}

	public String getHospital_intro() {
		return hospital_intro;
	}

	public void setHospital_intro(String hospital_intro) {
		this.hospital_intro = hospital_intro;
	}

	public String getHospital_logo() {
		return hospital_logo;
	}

	public void setHospital_logo(String hospital_logo) {
		this.hospital_logo = hospital_logo;
	}

	public String getHospital_lat() {
		return hospital_lat;
	}

	public void setHospital_lat(String hospital_lat) {
		this.hospital_lat = hospital_lat;
	}

	public String getHospital_lng() {
		return hospital_lng;
	}

	public void setHospital_lng(String hospital_lng) {
		this.hospital_lng = hospital_lng;
	}

	public List<SimpleDepartment> getListDepart() {
		return listDepart;
	}

	public void setListDepart(List<SimpleDepartment> listDepart) {
		this.listDepart = listDepart;
	}

	public String getHospital_area() {
		return hospital_area;
	}

	public void setHospital_area(String hospital_area) {
		this.hospital_area = hospital_area;
	}

	public int getIs_pass() {
		return is_pass;
	}

	public void setIs_pass(int is_pass) {
		this.is_pass = is_pass;
	}

	public int getHospital_type() {
		return hospital_type;
	}

	public void setHospital_type(int hospital_type) {
		this.hospital_type = hospital_type;
	}
	
}
