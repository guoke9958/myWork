package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.Doctor;

public interface DoctorDao {

	
	public List<Doctor> getAllDoctor();
	
	public void updateDoctorDepart(Doctor doctor);
	
}
