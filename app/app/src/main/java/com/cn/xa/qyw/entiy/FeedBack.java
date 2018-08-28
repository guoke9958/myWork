package com.cn.xa.qyw.entiy;

import java.sql.Timestamp;

/**
 * Created by 409160 on 2016/12/9.
 */
public class FeedBack {

    private int id;
    private String name;
    private String content;
    private String question;
    private Timestamp createTime;

    public FeedBack() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
