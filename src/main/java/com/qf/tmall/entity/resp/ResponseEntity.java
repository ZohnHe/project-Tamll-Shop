package com.qf.tmall.entity.resp;

import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;

public class ResponseEntity {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("成功状态：1=成功 0=失败")
    private String success;
    @ApiModelProperty("返回消息")
    private String msg;
    @ApiModelProperty("缓存ID")
    private String sessionId;
    @ApiModelProperty("文件信息")
    private String fileName;

    public ResponseEntity() {
    }

    public ResponseEntity(String success, String msg, String sessionId) {
        this.success = success;
        this.msg = msg;
        this.sessionId = sessionId;
    }

    public ResponseEntity(String success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Ignore
    private String userType;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
