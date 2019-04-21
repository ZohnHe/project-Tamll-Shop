package com.qf.tmall.service;


import com.github.pagehelper.PageInfo;
import com.qf.tmall.entity.OrderGroup;
import com.qf.tmall.entity.ProductOrder;
import com.qf.tmall.util.OrderUtil;
import com.qf.tmall.util.PageUtil;

import java.util.Date;
import java.util.List;

public interface ProductOrderService {
    boolean add(ProductOrder productOrder);
    boolean update(ProductOrder productOrder);
    boolean deleteList(Integer[] productOrder_id_list);

    List<ProductOrder> getList(ProductOrder productOrder, Byte[] productOrder_status_array, OrderUtil orderUtil, PageUtil pageUtil);

    List<OrderGroup> getTotalByDate(Date beginDate, Date endDate);

    ProductOrder get(Integer productOrder_id);
    ProductOrder getByCode(String productOrder_code);
    Integer getTotal(ProductOrder productOrder, Byte[] productOrder_status_array);
    //获取前10条订单
    PageInfo<ProductOrder> getProductOrderList(int pageNum, int pageSize);
}
