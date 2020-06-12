package com.zb.ssm.service.impl;

import com.github.pagehelper.PageHelper;
import com.zb.ssm.dao.IOrdersDao;
import com.zb.ssm.domain.Orders;
import com.zb.ssm.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrdersDao ordersDao;
    @Override
    public List<Orders> findAll(int page,int size) throws Exception {
        //参数PageNum代表页码值，参数pageSize代表每页显示条数,必须在分页代码之前
        PageHelper.startPage(page,size);
        return ordersDao.findAll();
    }

    @Override
    public Orders findById(String ordersId) throws Exception {
        return ordersDao.findById(ordersId);
    }
}
