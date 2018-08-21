package com.xa.qyw.dao;


import java.util.List;

import com.xa.qyw.entiy.Voucher;

public interface UserVoucherDao {
	
	public List<Voucher> getVoucherCount(String userId);
	
	public void insertVoucher(Voucher voucher);
	
	public void updateVoucher(Voucher voucher);
	
	public int getVoucherUserTotal(String userId);

}
