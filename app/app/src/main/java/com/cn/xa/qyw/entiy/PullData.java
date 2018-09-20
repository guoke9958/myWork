package com.cn.xa.qyw.entiy;

/**
 * Created by amoldZhang on 2018/9/20.
 */

public class PullData {
    private Integer id;
    private Integer pageSize;
    private Integer pageCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}
