package com.xa.qyw.service;

import java.util.List;

import com.xa.qyw.entiy.Voucher;

public interface UserVoucherService {

	
public List<Voucher> getVoucherCount(String userId);
	
	public void insertVoucher(Voucher voucher);
	
	public void updateVoucher(Voucher voucher);
	
	public int getVoucherUserTotal(String userId);
	
}
