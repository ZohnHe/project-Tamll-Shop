package com.qf.tmall.entity.commons;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {

    private String code = ResponseCode.OK.getCode();
    private String msg = ResponseCode.OK.getDesc();
    private T data;

    public BaseResponse(T data){
        this.data = data;
    }
    public BaseResponse(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
