package com.cc.crm.workbench.service;

import com.cc.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCustomerName(String name);
}
