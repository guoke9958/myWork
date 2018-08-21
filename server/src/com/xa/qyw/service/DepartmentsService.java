package com.xa.qyw.service;

import java.util.List;

import com.xa.qyw.entiy.Departments;
import com.xa.qyw.entiy.app.Area;
import com.xa.qyw.entiy.app.DepartmentsInfo;

public interface DepartmentsService {

	
	public void addDepartments(Departments departments);
	
	public Departments getDepartmentsByName(Departments departments);
	
	public void updateDepartmentsName(Departments departments);
	
	public List<DepartmentsInfo> getAllDepartmentsByArea(Area area);
	
}
