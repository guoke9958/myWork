package com.cn.xa.qyw.entiy;

import java.sql.Timestamp;

public class NewsDetail {
	
	private int newsId;
	private String title;
	private String author;
	private String source;
	private String content;
	private int isReprint;
	private int browseCount;
	private int newsType;
	private String newsPhoto;
	private Timestamp createTime;


	private String tumb;  //图片 路径
	private int articleId;  //轮播图id
	private String publishUrl; //轮播详情


	
	public NewsDetail() {
		super();
	}

	public String getTumb() {
		return tumb;
	}

	public void setTumb(String tumb) {
		this.tumb = tumb;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public String getPublishUrl() {
		return publishUrl;
	}

	public void setPublishUrl(String publishUrl) {
		this.publishUrl = publishUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getIsReprint() {
		return isReprint;
	}

	public void setIsReprint(int isReprint) {
		this.isReprint = isReprint;
	}

	public int getNewsType() {
		return newsType;
	}

	public void setNewsType(int newsType) {
		this.newsType = newsType;
	}

	public String getNewsPhoto() {
		return newsPhoto;
	}

	public void setNewsPhoto(String newsPhoto) {
		this.newsPhoto = newsPhoto;
	}

	
	
}
