package com.cn.xa.qyw.entiy;

/**
 * Created by Administrator on 2016/9/12.
 */
public class SelectGodItem {
    private int count;
    private boolean isSelected;

    public SelectGodItem() {
    }

    public SelectGodItem(int count, boolean isSelected) {
        this.count = count;
        this.isSelected = isSelected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
