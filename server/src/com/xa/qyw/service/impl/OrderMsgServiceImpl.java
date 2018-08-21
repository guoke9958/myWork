package com.xa.qyw.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.OrderMsgDao;
import com.xa.qyw.entiy.OrderMsg;
import com.xa.qyw.service.OrderMsgService;

@Service("orderMsgService")
public class OrderMsgServiceImpl implements OrderMsgService{

	@Resource(name = "orderMsgDao")
	private OrderMsgDao mOrderMsgDao;
	
	@Override
	public void addOrderMsg(OrderMsg msg) {
		mOrderMsgDao.addOrderMsg(msg);
	}

}
