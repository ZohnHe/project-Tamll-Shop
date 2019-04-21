package com.qf.tmall.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.qf.tmall.controller.BaseController;
import com.qf.tmall.entity.Admin;
import com.qf.tmall.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author ACI
 * @Title: AdminLoginController
 * @ProjectName Tmall_shop
 * @Description: TODO
 * @date 2019/2/19 10:27
 */
@Api(tags = "后端登录模块")
@Controller
public class AdminLoginController extends BaseController {

    @Resource(name = "adminService")
    private AdminService adminService;
    //转到后台管理-登录页
    @ApiOperation(value = "转到后台管理-登录页")
    @RequestMapping("admin/login")
    public String goToPage(){
        logger.info("转到后台管理-登录页");
        return "admin/loginPage";
    }

    //登陆验证-ajax
    @ApiOperation(value = "登陆验证-ajax")
    @ResponseBody
    @RequestMapping(value = "admin/login/doLogin",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String checkLogin(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        logger.info("管理员登录验证");
        Admin admin = adminService.login(username,password);


        JSONObject object = new JSONObject();
        if(admin == null){
            logger.info("登录验证失败");
            object.put("success",false);
        } else {
            logger.info("登录验证成功，管理员ID传入会话");
            request.getSession().setAttribute("admin", admin);
            request.getSession().setAttribute("adminId",admin.getAdmin_id());
            object.put("success",true);
        }

        return object.toJSONString();
    }
    //获取管理员信息-ajax
    @ApiOperation(value = "获取管理员信息-ajax")
    @ResponseBody
    @RequestMapping(value = "admin/login/profile_picture",method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public String getAdminProfilePicture(@RequestParam String username){
        logger.info("根据用户名获取管理员信息");
        Admin admin = adminService.get(username,null);

        JSONObject object = new JSONObject();
        if(admin == null){
            logger.info("未找到信息");
            object.put("success",false);
        } else {
            logger.info("成功获取信息");
            object.put("success",true);
            object.put("srcString",admin.getAdmin_profile_picture_src());
        }
        return object.toJSONString();
    }

    @ApiOperation(value = "获取管理员信息")
    @ResponseBody
    @GetMapping(value = "admin/getInfo")
    public Admin info(HttpServletRequest request) {

        Admin login = (Admin) request.getSession().getAttribute("admin");

        System.out.println(login);
        return login;
    }
}
