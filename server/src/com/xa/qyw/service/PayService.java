package com.xa.qyw.service;

import com.xa.qyw.entiy.TradeNo;

public interface PayService {

	public void saveTradeNo(TradeNo tradeNo);

	public void updateTradeNoStatus(TradeNo tardeNo);

	public TradeNo getTradeByNo(TradeNo tardeNo);
	
}
