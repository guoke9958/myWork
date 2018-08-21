package com.xa.qyw.service;

import com.xa.qyw.entiy.MsgState;

public interface MsgService {
	
	public void insertMsgSendState(MsgState state);
	
	public void updateMsgState(MsgState state);
	
	public MsgState getMsgState(MsgState state);
	
	public void updateIsUsed(int id);

}
