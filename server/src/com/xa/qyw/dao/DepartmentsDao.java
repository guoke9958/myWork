package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.Departments;
import com.xa.qyw.entiy.app.Area;
import com.xa.qyw.entiy.app.DepartmentsInfo;

public interface DepartmentsDao {
	
	public void addDepartments(Departments departments);
	
	public Departments getDepartmentsByName(Departments departments);
	
	public void updateDepartmentsName(Departments departments);
	
	public List<DepartmentsInfo> getAllDepartmentsByArea(Area area);
	
}
