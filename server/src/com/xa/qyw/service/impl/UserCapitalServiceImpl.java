package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.UserCapitalDao;
import com.xa.qyw.entiy.CapitalHistory;
import com.xa.qyw.entiy.Order;
import com.xa.qyw.entiy.RechargeGift;
import com.xa.qyw.entiy.UserCapital;
import com.xa.qyw.service.UserCapitalService;

@Service("userCapitalService")
public class UserCapitalServiceImpl implements UserCapitalService{

	@Resource(name = "userCapitalDao")
	UserCapitalDao userCapitalDao;
	
	@Override
	public void insertUserCapital(UserCapital capital) {
		userCapitalDao.insertUserCapital(capital);
	}

	@Override
	public UserCapital getUserCapital(UserCapital userId) {
		return userCapitalDao.getUserCapital(userId);
	}

	@Override
	public List<CapitalHistory> getCapitalHistory(CapitalHistory history) {
		return userCapitalDao.getCapitalHistory(history);
	}

	@Override
	public void updateCapitalRecharge(UserCapital capital) {
		userCapitalDao.updateCapitalRecharge(capital);
	}

	@Override
	public void insertUserCapitalHistory(CapitalHistory history) {
		userCapitalDao.insertUserCapitalHistory(history);
	}

	@Override
	public void insertRechargeGift(RechargeGift gift) {
		userCapitalDao.insertRechargeGift(gift);
	}

	@Override
	public void updateRechargeGift(RechargeGift gift) {
		userCapitalDao.updateRechargeGift(gift);
	}

	@Override
	public void updateExpandGift(RechargeGift gift) {
		userCapitalDao.updateExpandGift(gift);
	}

	@Override
	public RechargeGift getUserGift(RechargeGift gift) {
		return userCapitalDao.getUserGift(gift);
	}

	@Override
	public void updateCapitalInCome(UserCapital capital) {
		userCapitalDao.updateCapitalInCome(capital);
	}

	@Override
	public void updateCapitalExpand(UserCapital capital) {
		userCapitalDao.updateCapitalExpand(capital);
	}

	@Override
	public List<CapitalHistory> getCapitalHistoryInCome(CapitalHistory history) {
		return userCapitalDao.getCapitalHistoryInCome(history);
	}

	@Override
	public CapitalHistory getHongBaoDetail(String orderId) {
		return userCapitalDao.getHongBaoDetail(orderId);
	}

	@Override
	public Order getOrderById(String orderId) {
		return userCapitalDao.getOrderById(orderId);
	}

	@Override
	public void insertOrder(Order order) {
		userCapitalDao.insertOrder(order);
	}

}
