package com.xa.qyw.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.PayDao;
import com.xa.qyw.entiy.TradeNo;
import com.xa.qyw.service.PayService;

@Service("payService")
public class PayServiceImpl implements PayService{
	
	@Resource(name = "payDao")
	private PayDao payDao;

	@Override
	public void saveTradeNo(TradeNo tradeNo) {
		payDao.saveTradeNo(tradeNo);
	}

	@Override
	public void updateTradeNoStatus(TradeNo tardeNo) {
		payDao.updateTradeNoStatus(tardeNo);
	}

	@Override
	public TradeNo getTradeByNo(TradeNo tardeNo) {
		return payDao.getTradeByNo(tardeNo);
	}

}
