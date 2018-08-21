package com.cn.xa.qyw.entiy;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class ShowSearchResult {
    private String hospitalName;
    private List<SimpleDoctor> list;
    private int showAll;

    public ShowSearchResult() {
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public List<SimpleDoctor> getList() {
        return list;
    }

    public void setList(List<SimpleDoctor> list) {
        this.list = list;
    }

    public int isShowAll() {
        return showAll;
    }

    public void setShowAll(int showAll) {
        this.showAll = showAll;
    }
}
