package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.HospitalDetailInfo;
import com.xa.qyw.entiy.HospitalGrade;
import com.xa.qyw.entiy.UpdateHospitalName;

public interface HospitalDao {
	
	public HospitalDetailInfo getHospitalById(int id);
	
	public List<HospitalDetailInfo> getAllHospital();
	
	public void insertHospital(HospitalDetailInfo info);
	
	public HospitalDetailInfo queryHospitalByName(HospitalDetailInfo name);
	
	public List<HospitalDetailInfo> queryHospitalByCity(HospitalDetailInfo name);
	
	public List<HospitalDetailInfo> queryLikeHospitalByName(String name);
	
	public void insertUpdateHospitalName(UpdateHospitalName hospital);
	
	public List<HospitalGrade> getAllHospitalGrade();
}
