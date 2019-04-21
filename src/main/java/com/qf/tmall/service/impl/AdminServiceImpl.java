package com.qf.tmall.service.impl;

import com.qf.tmall.dao.AdminMapper;
import com.qf.tmall.entity.Admin;
import com.qf.tmall.service.AdminService;
import com.qf.tmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ACI
 * @Title: AdminServiceImpl
 * @ProjectName Tmall_shop
 * @Description: TODO
 * @date 2019/2/19 10:33
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {

    private AdminMapper adminMapper;
    @Resource(name = "adminMapper")
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }
    @Override
    public boolean add(Admin admin) {
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(Admin admin) {
        return adminMapper.updateOne(admin)>0;
    }

    @Override
    public List<Admin> getList(String admin_name, PageUtil pageUtil) {
        return null;
    }

    @Override
    public Admin get(String admin_name, Integer admin_id) {
        return adminMapper.selectOne(admin_name, admin_id);
    }

    @Override
    public Admin login(String admin_name, String admin_password) {
        return adminMapper.selectByLogin(admin_name, admin_password);
    }

    @Override
    public Integer getTotal(String admin_name) {
        return null;
    }
}
