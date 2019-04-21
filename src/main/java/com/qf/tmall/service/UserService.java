package com.qf.tmall.service;

import com.github.pagehelper.PageInfo;
import com.qf.tmall.entity.User;
import com.qf.tmall.util.OrderUtil;
import com.qf.tmall.util.PageUtil;


import java.util.List;
import java.util.Map;

public interface UserService {
    boolean add(User user);
    boolean update(User user);

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<User> getUserList(int pageNum, int pageSize);

    List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil);
    User get(Integer user_id);
    User login(String user_name, String user_password);
    Integer getTotal(User user);
}
