package com.xa.qyw.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.MsgStateDao;
import com.xa.qyw.entiy.MsgState;
import com.xa.qyw.service.MsgService;

@Service("msgService")
public class MsgStateServiceImpl implements MsgService{

	
	@Resource(name = "msgStateDao")
	private MsgStateDao msgDao;

	
	@Override
	public void insertMsgSendState(MsgState state) {
		msgDao.insertMsgSendState(state);
	}

	@Override
	public void updateMsgState(MsgState state) {
		msgDao.updateMsgState(state);
	}

	@Override
	public MsgState getMsgState(MsgState state) {
		return msgDao.getMsgState(state);
	}

	@Override
	public void updateIsUsed(int id) {
		msgDao.updateIsUsed(id);		
	}
	
}
