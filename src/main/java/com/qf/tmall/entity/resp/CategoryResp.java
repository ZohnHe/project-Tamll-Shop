package com.qf.tmall.entity.resp;

import com.qf.tmall.util.PageUtil;

public class CategoryResp<T> {
    private PageUtil pageUtil;
    private T data;

    public CategoryResp(T data) {
        this.data = data;
    }

    public CategoryResp() {
    }

    public PageUtil getPageUtil() {
        return pageUtil;
    }

    public void setPageUtil(PageUtil pageUtil) {
        this.pageUtil = pageUtil;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
