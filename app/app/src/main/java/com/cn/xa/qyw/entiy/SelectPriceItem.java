package com.cn.xa.qyw.entiy;

/**
 * Created by Administrator on 2016/8/21.
 */
public class SelectPriceItem {

    private int resourceId;
    private String title;
    private String tips;
    private String price;

    public SelectPriceItem() {
    }

    public SelectPriceItem(int resourceId, String title, String tips, String price) {
        this.resourceId = resourceId;
        this.title = title;
        this.tips = tips;
        this.price = price;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
