package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.NewDepartmentsDao;
import com.xa.qyw.entiy.AddDepartments;
import com.xa.qyw.entiy.DepartmentInfo;
import com.xa.qyw.entiy.app.SimpleDepartment;
import com.xa.qyw.service.NewDepartmentsService;

@Service("newDepartmentsService")
public class NewDepartmentsServiceImpl implements NewDepartmentsService{

	@Resource(name = "newDepartmentsDao")
	private NewDepartmentsDao newDepartmentsDao;
	
	
	@Override
	public void addDepartments(AddDepartments departments) {
		newDepartmentsDao.addToDepartments(departments);
	}

	@Override
	public AddDepartments getDepartmentsByName(AddDepartments departments) {
		return newDepartmentsDao.getDepartmentsByName(departments);
	}

	@Override
	public List<AddDepartments> getAllDepartments() {
		return newDepartmentsDao.getAllDepartments();
	}

	@Override
	public void addDepartmentInfo(DepartmentInfo info) {
		newDepartmentsDao.addDepartmentInfo(info);
	}

	@Override
	public DepartmentInfo getDepartmentInfoByHosptailIdAndDepartmentId(DepartmentInfo info) {
		return newDepartmentsDao.getDepartmentInfoByHosptailIdAndDepartmentId(info);
	}

	@Override
	public List<SimpleDepartment> getListDepartment(int hospitalId) {
		return newDepartmentsDao.getListDepartment(hospitalId);
	}

	@Override
	public List<AddDepartments> getDepartmentsByGrade(String grade) {
		return newDepartmentsDao.getDepartmentsByGrade(grade);
	}

}
