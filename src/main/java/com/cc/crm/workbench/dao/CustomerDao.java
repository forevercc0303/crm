package com.cc.crm.workbench.dao;

import com.cc.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<Customer> getCustomerName(String name);
}
