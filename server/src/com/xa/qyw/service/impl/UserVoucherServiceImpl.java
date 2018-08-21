package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.UserVoucherDao;
import com.xa.qyw.entiy.Voucher;
import com.xa.qyw.service.UserVoucherService;

@Service("userVoucherService")
public class UserVoucherServiceImpl implements UserVoucherService{

	@Resource(name = "userVoucherDao")
	private UserVoucherDao userVoucherDao;
	
	@Override
	public List<Voucher> getVoucherCount(String userId) {
		return userVoucherDao.getVoucherCount(userId);
	}

	@Override
	public void insertVoucher(Voucher voucher) {
		userVoucherDao.insertVoucher(voucher);
	}

	@Override
	public void updateVoucher(Voucher voucher) {
		userVoucherDao.updateVoucher(voucher);
	}

	@Override
	public int getVoucherUserTotal(String userId) {
		return userVoucherDao.getVoucherUserTotal(userId);
	}

}
