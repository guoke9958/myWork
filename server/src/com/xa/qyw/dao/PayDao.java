package com.xa.qyw.dao;

import com.xa.qyw.entiy.TradeNo;

public interface PayDao {

	public void saveTradeNo(TradeNo tradeNo);

	public void updateTradeNoStatus(TradeNo tardeNo);

	public TradeNo getTradeByNo(TradeNo tardeNo);

}
