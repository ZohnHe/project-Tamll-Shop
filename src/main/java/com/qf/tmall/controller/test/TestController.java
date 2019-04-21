package com.qf.tmall.controller.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ACI
 * @Title: TestController
 * @ProjectName Tmall_shop
 * @Description: TODO
 * @date 2019/2/18 21:57
 */
@Api(tags = "测试Controller")
@Controller
public class TestController {

    @ApiOperation(value = "跳转测试页面")
    @GetMapping(value = "test")
    public String test() {
        return "admin/test";
    }
}
