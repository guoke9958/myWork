package com.cn.xa.qyw.entiy;

/**
 * Created by Administrator on 2016/11/28.
 */
public class NewsItem {

    public static int IMAGE = 1;
    public static int SPAN = 2;
    public static int STRONG = 3;
    public static int P = 4;


    private int type;
    private String content;

    public NewsItem() {
    }

    public NewsItem(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
