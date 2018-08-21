package com.xa.qyw.dao;

import java.util.List;

import com.xa.qyw.entiy.VideoDetail;
import com.xa.qyw.entiy.VideoType;

public interface VideoDao {
	
	public void insertVideDetail(VideoDetail detail);

	public List<VideoType> getAllVideoType();
	
	public List<VideoDetail> getVideoByType(int typeId);
	
	public VideoDetail getVideoDetail(int id);
	
}
