package com.xa.qyw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xa.qyw.dao.VideoDao;
import com.xa.qyw.entiy.VideoDetail;
import com.xa.qyw.entiy.VideoType;
import com.xa.qyw.service.VideoService;

@Service("videoService")
public class VideoServiceImpl implements VideoService{
	
	@Resource(name = "videoDao")
	private VideoDao mVideoDao;
	
	@Override
	public List<VideoType> getAllVideoType() {
		return mVideoDao.getAllVideoType();
	}

	@Override
	public List<VideoDetail> getVideoByType(int typeId) {
		return mVideoDao.getVideoByType(typeId);
	}

	@Override
	public VideoDetail getVideoDetail(int id) {
		return mVideoDao.getVideoDetail(id);
	}

	@Override
	public void insertVideDetail(VideoDetail detail) {
		mVideoDao.insertVideDetail(detail);
	}

}
