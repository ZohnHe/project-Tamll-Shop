package com.qf.tmall.dao;


import com.qf.tmall.entity.OrderGroup;
import com.qf.tmall.entity.ProductOrder;
import com.qf.tmall.util.OrderUtil;
import com.qf.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProductOrderMapper {
    Integer insertOne(@Param("productOrder") ProductOrder productOrder);
    Integer updateOne(@Param("productOrder") ProductOrder productOrder);
    Integer deleteList(@Param("productOrder_id_list") Integer[] productOrder_id_list);

    List<ProductOrder> select(@Param("productOrder") ProductOrder productOrder, @Param("productOrder_status_array") Byte[] productOrder_status_array, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil);
    ProductOrder selectOne(@Param("productOrder_id") Integer productOrder_id);
    ProductOrder selectByCode(@Param("productOrder_code") String productOrder_code);
    Integer selectTotal(@Param("productOrder") ProductOrder productOrder, @Param("productOrder_status_array") Byte[] productOrder_status_array);
    List<OrderGroup> getTotalByDate(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);
    //获取所有的分页信息
    List<ProductOrder> getProductOrderList();
}
