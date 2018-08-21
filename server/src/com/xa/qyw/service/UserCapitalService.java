package com.xa.qyw.service;

import java.util.List;

import com.xa.qyw.entiy.CapitalHistory;
import com.xa.qyw.entiy.Order;
import com.xa.qyw.entiy.RechargeGift;
import com.xa.qyw.entiy.UserCapital;

public interface UserCapitalService {

	public void insertUserCapital(UserCapital capital);
	
	public UserCapital getUserCapital(UserCapital userId);
	
	public List<CapitalHistory> getCapitalHistory(CapitalHistory history);
	
	public List<CapitalHistory> getCapitalHistoryInCome(CapitalHistory history);
	
	public void updateCapitalRecharge(UserCapital capital);
	
	public void insertUserCapitalHistory(CapitalHistory history);
	
	public void insertRechargeGift(RechargeGift gift);
	
	public void updateRechargeGift(RechargeGift gift);
	
	public void updateExpandGift(RechargeGift gift);
	
	public RechargeGift getUserGift(RechargeGift gift);
	
	public void updateCapitalInCome(UserCapital capital);
	
	public void updateCapitalExpand(UserCapital capital);
	
	public CapitalHistory getHongBaoDetail(String orderId);
	
	public Order getOrderById(String orderId);
	
	public void insertOrder(Order order);
}
