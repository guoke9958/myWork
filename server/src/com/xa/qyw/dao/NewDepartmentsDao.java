package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.AddDepartments;
import com.xa.qyw.entiy.DepartmentInfo;
import com.xa.qyw.entiy.app.SimpleDepartment;

public interface NewDepartmentsDao {

	public void addToDepartments(AddDepartments departments);
	
	public AddDepartments getDepartmentsByName(AddDepartments departments);
	
	public List<AddDepartments> getAllDepartments();
	
	public void addDepartmentInfo(DepartmentInfo info);
	
	public DepartmentInfo getDepartmentInfoByHosptailIdAndDepartmentId(DepartmentInfo info);
	
	public List<SimpleDepartment> getListDepartment(int hospitalId);
	
	public List<AddDepartments> getDepartmentsByGrade(String grade);
	
}
