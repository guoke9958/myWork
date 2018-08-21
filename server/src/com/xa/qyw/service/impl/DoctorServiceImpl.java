package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.NewDoctorDao;
import com.xa.qyw.entiy.DepartmentInfo;
import com.xa.qyw.entiy.app.DoctorDetailInfo;
import com.xa.qyw.entiy.app.SearchDoctorByDepartmentAndCity;
import com.xa.qyw.entiy.app.SimpleDoctor;
import com.xa.qyw.service.DoctorService;

@Service("doctorService")
public class DoctorServiceImpl implements DoctorService {
	
	@Resource(name = "newDoctorDao")
	private NewDoctorDao dao;

	@Override
	public List<SimpleDoctor> getDoctorByDepartmentsId(SearchDoctorByDepartmentAndCity departmentId) {
		List<SimpleDoctor> doctors = dao.getDoctorByDepartmentsId(departmentId);
		return doctors;
	}

	@Override
	public DoctorDetailInfo getDocotorDetailInfo(String userId) {
		return dao.getDocotorDetailInfo(userId);
	}

	@Override
	public List<DoctorDetailInfo> getAllDoctor() {
		return dao.getAllDoctor();
	}

	@Override
	public List<SimpleDoctor> getDoctorByDepartmentsIdAndHospitalId(
			DepartmentInfo info) {
		return dao.getDoctorByDepartmentsIdAndHospitalId(info);
	}

	@Override
	public void updateDoctorIntro(DoctorDetailInfo info) {
		dao.updateDoctorIntro(info);
	}

	@Override
	public List<SimpleDoctor> getUserByDepartmentsId(SearchDoctorByDepartmentAndCity info) {
		return dao.getUserByDepartmentsId(info);
	}

}
