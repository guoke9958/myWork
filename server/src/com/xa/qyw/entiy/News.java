package com.xa.qyw.entiy;

import java.sql.Timestamp;

public class News {
	
	private int id;
	private String newsFirst;
	private String newsSecond;
	private String newsThree;
	private int isComplete;
	private Timestamp createTime;
	
	public News() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNewsFirst() {
		return newsFirst;
	}

	public void setNewsFirst(String newsFirst) {
		this.newsFirst = newsFirst;
	}

	public String getNewsSecond() {
		return newsSecond;
	}

	public void setNewsSecond(String newsSecond) {
		this.newsSecond = newsSecond;
	}

	public String getNewsThree() {
		return newsThree;
	}

	public void setNewsThree(String newsThree) {
		this.newsThree = newsThree;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}
	
	
}
