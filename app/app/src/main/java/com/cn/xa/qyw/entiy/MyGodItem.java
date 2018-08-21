package com.cn.xa.qyw.entiy;

/**
 * Created by Administrator on 2016/9/4.
 */
public class MyGodItem {

    public static int GOD_TYPE_YU_E = 4;
    public static int GOD_TYPE_SHOU_RU = 6;
    public static int GOD_TYPE_XIAO_FEI = 2;
    public static int GOD_TYPE_CHONG_ZHI = 1;

    private int type;
    private String name;
    private String count;
    private int resourceId;

    public MyGodItem() {
    }

    public MyGodItem(int type, String name, String count,int resourceId) {
        this.type = type;
        this.name = name;
        this.count = count;
        this.resourceId = resourceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
