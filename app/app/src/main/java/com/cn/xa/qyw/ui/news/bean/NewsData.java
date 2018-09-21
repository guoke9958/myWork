package com.cn.xa.qyw.ui.news.bean;

/**
 * Created by amoldZhang on 2018/9/17.
 */

public class NewsData {

    private Integer articleId;
    private String source;  //来源
    private String summary; // 简讯
    private String title;   //标题
    private String tumb;  // 图片链接
    private String clickNum; // 点击量
    private Integer videoState; //  是否有视频  1有  0 无
    private Integer isSpecial; //  是否头条  1是  0 不是
    private long updateTime; //  刷新时间

    public NewsData() {
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getSource() {
        return source;
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    public Integer getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(Integer isSpecial) {
        this.isSpecial = isSpecial;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTumb() {
        return tumb;
    }

    public void setTumb(String tumb) {
        this.tumb = tumb;
    }


    public Integer getVideoState() {
        return videoState;
    }

    public void setVideoState(Integer videoState) {
        this.videoState = videoState;
    }
}
