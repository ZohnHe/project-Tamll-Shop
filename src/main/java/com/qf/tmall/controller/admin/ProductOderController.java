package com.qf.tmall.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qf.tmall.controller.BaseController;
import com.qf.tmall.entity.Address;
import com.qf.tmall.entity.Product;
import com.qf.tmall.entity.ProductOrder;
import com.qf.tmall.entity.ProductOrderItem;
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
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@Api(tags = "显示-订单管理")
@Controller
public class ProductOderController extends BaseController {
    @Resource
    private ProductOrderService productOrderService;
    @Resource
    private AddressService addressService;
    @Resource
    private UserService userService;
    @Resource
    private ProductService productService;
    @Resource
    private ProductOrderItemService productOrderItemService;
    @Resource
    private ProductImageService productImageService;


    @ApiOperation(value = "订单分页")
    @ResponseBody
    @PostMapping(value = "admin/orderlist")
    public BaseResponse<PageListResp<ProductOrder>> getList(@RequestBody PageListReq pageListReq){
        PageInfo<ProductOrder> data = productOrderService.getProductOrderList(pageListReq.getPageNum(), pageListReq.getPageSize());
        PageListResp<ProductOrder> pageListResp = new PageListResp(data);
        pageListResp.setPageNum(pageListReq.getPageNum());
        pageListResp.setPageSize(pageListReq.getPageSize());
        pageListResp.setTotalPage(data.getTotal());
        //(总记录数+分页数-1)/分页数
        long totalPage = (pageListResp.getTotalRecord()+pageListResp.getPageNum()-1)/pageListReq.getPageNum();
        pageListResp.setTotalRecord(totalPage);
        return new BaseResponse(pageListResp);
    }

    //转到后台管理-订单页-ajax
    @RequestMapping(value = "admin/order", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map){
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }
        return "admin/orderManagePage";
    }

    //转到后台管理-订单详情页
    @ApiOperation(value = "转到后台管理-订单详情页")
    @RequestMapping(value = "admin/order/{oid}", method = RequestMethod.GET)
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer oid/* 订单ID */) {
        session.setAttribute("oid", oid);
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }
        logger.info("转到后台管理-订单详情页");
        return "admin/include/orderDetails";
    }

    @ApiOperation(value = "转到后台管理-订单详情页 - 返回数据")
    @ResponseBody
    @RequestMapping(value = "admin/order/getdata", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String goToDetailsPageGetData(HttpSession session) {

        Integer oid = (Integer) session.getAttribute("oid");
        JSONObject jsonObject = new JSONObject();
        logger.info("获取order_id为{}的订单信息",oid);
        ProductOrder order = productOrderService.get(oid);
        logger.info("获取订单详情-地址信息");
        Address address = addressService.get(order.getProductOrder_address().getAddress_areaId());
        Stack<String> addressStack = new Stack<>();
        //详细地址
        addressStack.push(order.getProductOrder_detail_address());
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
        logger.warn("订单地址字符串：{}", builder);
        order.setProductOrder_detail_address(builder.toString());
        logger.info("获取订单详情-用户信息");
        order.setProductOrder_user(userService.get(order.getProductOrder_user().getUser_id()));
        logger.info("获取订单详情-订单项信息");
        List<ProductOrderItem> productOrderItemList = productOrderItemService.getListByOrderId(oid, null);
        if (productOrderItemList != null) {
            logger.info("获取订单详情-订单项对应的产品信息");
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
        order.setProductOrderItemList(productOrderItemList);
        jsonObject.put("order", order);
        logger.info("转到后台管理-订单详情页-返回数据");
        return jsonObject.toJSONString();
    }

    //更新订单信息-ajax
    @ResponseBody
    @RequestMapping(value = "admin/order/{order_id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public String updateOrder(@PathVariable("order_id") String order_id) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合订单信息");
        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_id(Integer.valueOf(order_id))
                .setProductOrder_status((byte) 2)
                .setProductOrder_delivery_date(new Date());
        logger.info("更新订单信息，订单ID值为：{}", order_id);
        boolean yn = productOrderService.update(productOrder);
        if (yn) {
            logger.info("更新成功！");
            jsonObject.put("success", true);
        } else {
            logger.info("更新失败！事务回滚");
            jsonObject.put("success", false);
            throw new RuntimeException();
        }
        jsonObject.put("order_id", order_id);
        return jsonObject.toJSONString();
    }

}
