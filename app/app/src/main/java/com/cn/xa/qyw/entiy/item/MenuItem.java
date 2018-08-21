package com.cn.xa.qyw.entiy.item;

/**
 * Created by 409160 on 2016/7/7.
 */
public class MenuItem {

    public final static int TYPE_DISCOVER_NEWS = 1;
    public final static int TYPE_DISCOVER_DOCTOR_ARTICLE = 2;
    public final static int TYPE_DISCOVER_FIRST_AID = 3;
    public final static int TYPE_DISCOVER_NTFS = 4;
    public final static int TYPE_DISCOVER_LOTTERY = 5;
    public final static int TYPE_DISCOVER_LIVE = 6;

    public final static int TYPE_HUIYUAN_XUZHI = 7;
    public final static int TYPE_BASE_INFO_UPDATE = 8;
    public final static int TYPE_HUIZHEN_MANAGER = 9;
    public final static int TYPE_NEW_GONGGAO = 10;

    private int type;
    private String name;
    private int iconResourceId;

    public MenuItem() {
    }

    public MenuItem(int type, String name, int iconResourceId) {
        this.type = type;
        this.name = name;
        this.iconResourceId = iconResourceId;
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

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }
}
