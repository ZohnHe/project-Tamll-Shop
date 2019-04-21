package com.qf.tmall.entity.commons;

/**
 * 返回状态码与返回信息
 */
public enum  ResponseCode {

     OK("200","正常"),
     FAIL("500","失败");


    private String code;
    private String desc;

    private ResponseCode(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }}
