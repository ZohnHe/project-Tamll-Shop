package com.qf.tmall.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.qf.tmall.controller.BaseController;
import com.qf.tmall.entity.Address;
import com.qf.tmall.entity.Product;
import com.qf.tmall.entity.ProductOrderItem;
import com.qf.tmall.entity.User;
import com.qf.tmall.entity.commons.BaseResponse;
import com.qf.tmall.entity.commons.PageListReq;
import com.qf.tmall.entity.commons.PageListResp;
import com.qf.tmall.service.*;
import com.qf.tmall.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author ACI
 * @Title: UserController
 * @ProjectName Tmall_shop
 * @Description: TODO
 * @date 2019/2/19 21:22
 */
@Api(tags = "后台管理-用户页")
@Controller
public class UserController extends BaseController {

    @Resource(name="userService")
    private UserService userService;
    @Resource
    private AddressService addressService;
    @Resource
    private ProductService productService;
    @Resource
    private ProductOrderService productOrderService;
    @Resource
    private ProductOrderItemService productOrderItemService;
    @Resource
    private ProductImageService productImageService;

    //转到后台管理-用户页-ajax
    @ApiOperation(value = "转到后台管理-用户页")
    @RequestMapping(value = "admin/user", method = RequestMethod.GET)
    public String goUserManagePage(HttpSession session, Map<String, Object> map){
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }
        logger.info("转到后台管理-用户页-ajax方式");
        return "admin/userManagePage";
    }

    /**
     * 获取分页信息
     * @param request
     * @param pageListReq 分页请求信息
     * @return
     */
    @ApiOperation(value = "分页展示数据")
    @ResponseBody
    @PostMapping(value = "admin/userlist")
    public BaseResponse<PageListResp<User>> getUserList(HttpServletRequest request,@RequestBody PageListReq pageListReq){
        //获取分页信息
        PageInfo<User> data = userService.getUserList(pageListReq.getPageNum(), pageListReq.getPageSize());
        PageListResp<User> pageListResp = new PageListResp(data);
        pageListResp.setPageNum(pageListReq.getPageSize());
        pageListResp.setPageSize(pageListReq.getPageNum());
        pageListResp.setTotalPage(data.getTotal());
        //(总记录数+分页数-1)/分页数
        long totalPage = (pageListResp.getTotalRecord()+pageListResp.getPageSize()-1)/pageListResp.getPageSize();
        pageListResp.setTotalRecord(totalPage);

        return new BaseResponse(pageListResp);
    }


    //转到后台管理-用户详情页
    @ApiOperation(value = "转到后台管理-用户详情页")
    @RequestMapping(value = "admin/user/{uid}", method = RequestMethod.GET)
    public String getUserById(HttpSession session, Map<String,Object> map, @PathVariable Integer uid/* 用户ID */){
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }
        session.setAttribute("uid", uid);
        logger.info("转到后台管理-用户详情页");
        return "admin/include/userDetails";
    }

    @ResponseBody
    @ApiOperation(value = "转到后台管理-用户详情页 -返回数据")
    @RequestMapping(value = "admin/user/getdata", method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public String getUserByIdGetData(HttpSession session){
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }

        JSONObject jsonObject = new JSONObject();
        Integer uid = (Integer) session.getAttribute("uid");
        logger.info("获取user_id为{}的用户信息",uid);
        User user = userService.get(uid);
        logger.info("获取用户详情-所在地地址信息");
        Address address = addressService.get(user.getUser_address().getAddress_areaId());
        Stack<String> addressStack = new Stack<>();
        //最后一级地址
        addressStack.push(address.getAddress_name() + " ");
        //如果不是第一级地址
        while (!address.getAddress_areaId().equals(address.getAddress_regionId().getAddress_areaId())) {
            address = addressService.get(address.getAddress_regionId().getAddress_areaId());
            addressStack.push(address.getAddress_name() + " ");
        }
        StringBuilder builder = new StringBuilder();
        while (!addressStack.empty()) {
            builder.append(addressStack.pop());
        }
        logger.info("所在地地址字符串：{}", builder);
        user.setUser_address(new Address().setAddress_name(builder.toString()));

        logger.info("获取用户详情-家乡地址信息");
        address = addressService.get(user.getUser_homeplace().getAddress_areaId());
        //最后一级地址
        addressStack.push(address.getAddress_name() + " ");
        //如果不是第一级地址
        while (!address.getAddress_areaId().equals(address.getAddress_regionId().getAddress_areaId())) {
            address = addressService.get(address.getAddress_regionId().getAddress_areaId());
            addressStack.push(address.getAddress_name() + " ");
        }
        builder = new StringBuilder();
        while (!addressStack.empty()) {
            builder.append(addressStack.pop());
        }
        logger.info("家乡地址字符串：{}", builder);
        user.setUser_homeplace(new Address().setAddress_name(builder.toString()));

        logger.info("获取用户详情-购物车订单项信息");
        List<ProductOrderItem> productOrderItemList = productOrderItemService.getListByUserId(user.getUser_id(), null);
        if (productOrderItemList != null) {
            logger.info("获取用户详情-购物车订单项对应的产品信息");
            for (ProductOrderItem productOrderItem : productOrderItemList) {
                Integer productId = productOrderItem.getProductOrderItem_product().getProduct_id();
                logger.warn("获取产品ID为{}的产品信息", productId);
                Product product = productService.get(productId);
                if (product != null) {
                    logger.warn("获取产品ID为{}的第一张预览图片信息", productId);
                    product.setSingleProductImageList(productImageService.getList(productId, (byte) 0, new PageUtil(0, 1)));
                }
                productOrderItem.setProductOrderItem_product(product);
            }
        }
        user.setProductOrderItemList(productOrderItemList);

        if (user.getUser_realname() != null) {
            logger.info("用户隐私加密");
            user.setUser_realname(user.getUser_realname().substring(0, 1) + "*");
        } else {
            user.setUser_realname("未命名");
        }

        jsonObject.put("user",user);

        logger.info("转到后台管理-用户详情页-ajax方式 返回数据");
        return jsonObject.toJSONString();
    }

}
