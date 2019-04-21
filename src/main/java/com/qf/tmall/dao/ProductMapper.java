package com.qf.tmall.dao;


import com.qf.tmall.entity.Product;
import com.qf.tmall.util.OrderUtil;
import com.qf.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    Integer insertOne(@Param("product") Product product);
    Integer updateOne(@Param("product") Product product);

    List<Product> select(@Param("product") Product product, @Param("product_isEnabled_array") Byte[] product_isEnabled_array, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil,@Param("orderStr")String orderStr,@Param("productName")String productName);

    List<Product> selectTitle(@Param("product") Product product, @Param("pageUtil") PageUtil pageUtil);
    Product selectOne(@Param("product_id") Integer product_Id);
    Integer selectTotal(@Param("product") Product product, @Param("product_isEnabled_array") Byte[] product_isEnabled_array);

    List<Product> selectMoreList(@Param("product") Product product, @Param("product_isEnabled_array") Byte[] bytes, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil, @Param("product_name_split") String[] product_name_split);

    Integer selectMoreListTotal(@Param("product") Product product, @Param("product_isEnabled_array") Byte[] product_isEnabled_array, @Param("product_name_split") String[] product_name_split);
}
