package com.qf.tmall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.tmall.dao.UserMapper;
import com.qf.tmall.entity.User;
import com.qf.tmall.service.UserService;

import com.qf.tmall.util.OrderUtil;
import com.qf.tmall.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    @Resource(name = "userMapper")
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(User user) {
        return userMapper.insertOne(user)>0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(User user) {
        return userMapper.updateOne(user)>0;
    }

    @Override
    public PageInfo<User> getUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //TODO 获取所用用户数据
        List<User> data = userMapper.selectAll();
        return new PageInfo<User>(data);
    }



    @Override
    public List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil) {
        return userMapper.select(user,orderUtil,pageUtil);
    }

    @Override
    public User get(Integer user_id) {
        return userMapper.selectOne(user_id);
    }

    @Override
    public User login(String user_name, String user_password) {
        return userMapper.selectByLogin(user_name,user_password);
    }

    @Override
    public Integer getTotal(User user) {
        return userMapper.selectTotal(user);
    }
}
