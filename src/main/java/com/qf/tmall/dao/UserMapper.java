package com.qf.tmall.dao;

import com.qf.tmall.entity.User;

import com.qf.tmall.util.OrderUtil;
import com.qf.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {


    Integer insertOne(@Param("user") User user);

    Integer updateOne(@Param("user") User user);

    List<User> select(@Param("user") User user, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil);

    List<User> selectAll();

    User selectOne(@Param("user_id") Integer user_id);

    User selectByLogin(@Param("user_name") String user_name, @Param("user_password") String user_password);

    Integer selectTotal(@Param("user") User user);
}
