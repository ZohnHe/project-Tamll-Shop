package com.qf.tmall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;

/**
 * 基控制器
 * @Author ACI
 * @Version  1.0.0
 * @param
 * @Return
 * @Date 2019/2/19  9:48
 */
@Api(tags = "基类控制器")
@Controller
public class BaseController {
    //log4j2
    public Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    //检查管理员权限
    @ApiOperation("检查管理员权限")
    public Object checkAdmin(HttpSession session){
        Object o = session.getAttribute("adminId");
        if(o==null){
            logger.info("无管理权限，返回管理员登陆页");
            return null;
        }
        logger.info("权限验证成功，管理员ID：{}",o);
        return o;
    }

    //检查用户是否登录
    @ApiOperation("检查用户是否登录")
    public Object checkUser(HttpSession session){
        Object o = session.getAttribute("userId");
        if(o==null){
            logger.info("用户未登录");
            return null;
        }
        logger.info("用户已登录，用户ID：{}", o);
        return o;
    }
}
