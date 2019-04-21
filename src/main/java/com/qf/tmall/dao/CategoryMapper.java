package com.qf.tmall.dao;


import com.qf.tmall.entity.Category;
import com.qf.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    Integer insertOne(@Param("category") Category category);
    Integer updateOne(@Param("category") Category category);

    List<Category> select(@Param("category_name") String category_name, @Param("pageUtil") PageUtil pageUtil);
    Category selectOne(@Param("category_id") Integer category_id);
    Integer selectTotal(@Param("category_name") String category_name);
}