package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.HospitalDao;
import com.xa.qyw.entiy.HospitalDetailInfo;
import com.xa.qyw.entiy.HospitalGrade;
import com.xa.qyw.entiy.UpdateHospitalName;
import com.xa.qyw.service.HospitalService;

@Service("hospitalService")
public class HospitalServiceImpl implements HospitalService {

	@Resource(name = "hospitalDao")
	private HospitalDao hospitalDao;

	@Override
	public HospitalDetailInfo getHospitalById(int id) {
		return hospitalDao.getHospitalById(id);
	}

	@Override
	public List<HospitalDetailInfo> getAllHospital() {
		return hospitalDao.getAllHospital();
	}

	@Override
	public void insertHospital(HospitalDetailInfo info) {
		hospitalDao.insertHospital(info);
	}

	@Override
	public HospitalDetailInfo queryHospitalByName(HospitalDetailInfo name) {
		return hospitalDao.queryHospitalByName(name);
	}

	@Override
	public List<HospitalDetailInfo> queryLikeHospitalByName(String name) {
		return hospitalDao.queryLikeHospitalByName(name);
	}

	@Override
	public List<HospitalDetailInfo> queryHospitalByCity(HospitalDetailInfo name) {
		return hospitalDao.queryHospitalByCity(name);
	}

	@Override
	public void insertUpdateHospitalName(UpdateHospitalName hospital) {
		hospitalDao.insertUpdateHospitalName(hospital);
	}

	@Override
	public List<HospitalGrade> getAllHospitalGrade() {
		return hospitalDao.getAllHospitalGrade();
	}
	
	

}
