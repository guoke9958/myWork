package com.xa.qyw.service;

import java.util.List;

import com.xa.qyw.entiy.DepartmentInfo;
import com.xa.qyw.entiy.app.DoctorDetailInfo;
import com.xa.qyw.entiy.app.SearchDoctorByDepartmentAndCity;
import com.xa.qyw.entiy.app.SimpleDoctor;


public interface DoctorService {
	
	public List<SimpleDoctor> getDoctorByDepartmentsId(SearchDoctorByDepartmentAndCity departmentId);
	
	public List<SimpleDoctor> getDoctorByDepartmentsIdAndHospitalId(DepartmentInfo info);
	
	public DoctorDetailInfo getDocotorDetailInfo(String userId);
	
	public List<DoctorDetailInfo> getAllDoctor();
	
	public void updateDoctorIntro(DoctorDetailInfo info);
	
	public List<SimpleDoctor> getUserByDepartmentsId(SearchDoctorByDepartmentAndCity info);
}
