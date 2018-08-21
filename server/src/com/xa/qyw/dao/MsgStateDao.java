package com.xa.qyw.dao;

import com.xa.qyw.entiy.MsgState;

public interface MsgStateDao {

	public void insertMsgSendState(MsgState state);
	
	public void updateMsgState(MsgState state);
	
	public MsgState getMsgState(MsgState state);
	
	public void updateIsUsed(int id);
}
