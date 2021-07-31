package com.cc.crm.workbench.service.impl;

import com.cc.crm.utils.SqlSessionUtil;
import com.cc.crm.workbench.dao.CustomerDao;
import com.cc.crm.workbench.domain.Customer;
import com.cc.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    public List<Customer> getCustomerName(String name) {
        List<Customer> cList = customerDao.getCustomerName(name);
        return cList;
    }
}
