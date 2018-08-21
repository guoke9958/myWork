package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.DepartmentInfo;
import com.xa.qyw.entiy.Doctor;
import com.xa.qyw.entiy.app.DoctorDetailInfo;
import com.xa.qyw.entiy.app.SearchDoctorByDepartmentAndCity;
import com.xa.qyw.entiy.app.SimpleDoctor;

public interface NewDoctorDao {

	public void inserDocotrData(Doctor doctor);
	
	
	public List<SimpleDoctor> getDoctorByDepartmentsId(SearchDoctorByDepartmentAndCity departmentId);
	
	public void updateDoctorInfo(Doctor doctor);
	
	public DoctorDetailInfo getDocotorDetailInfo(String userId);
	
	public List<DoctorDetailInfo> getAllDoctor();
	
	public List<SimpleDoctor> getDoctorByDepartmentsIdAndHospitalId(DepartmentInfo info);
	
	public void updateDoctorIntro(DoctorDetailInfo info);
	
	public List<SimpleDoctor> getUserByDepartmentsId(SearchDoctorByDepartmentAndCity info);
}
