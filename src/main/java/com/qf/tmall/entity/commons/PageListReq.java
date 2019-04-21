package com.qf.tmall.entity.commons;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页请求参数
 */
public class PageListReq {
    @ApiModelProperty("当前页码")
    private int pageNum;
    @ApiModelProperty("分页数")
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
