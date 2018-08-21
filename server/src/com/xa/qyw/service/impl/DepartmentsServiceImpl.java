package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.DepartmentsDao;
import com.xa.qyw.entiy.Departments;
import com.xa.qyw.entiy.app.Area;
import com.xa.qyw.entiy.app.DepartmentsInfo;
import com.xa.qyw.service.DepartmentsService;

@Service("departmentsService")
public class DepartmentsServiceImpl implements DepartmentsService{

	@Resource(name = "departmentsDao")
	private DepartmentsDao departmentsDao;
	
	@Override
	public void addDepartments(Departments departments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Departments getDepartmentsByName(Departments departments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDepartmentsName(Departments departments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DepartmentsInfo> getAllDepartmentsByArea(Area area) {
		return departmentsDao.getAllDepartmentsByArea(area);
	}

}
