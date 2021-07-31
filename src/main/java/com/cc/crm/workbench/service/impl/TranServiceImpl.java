package com.cc.crm.workbench.service.impl;

import com.cc.crm.utils.DateTimeUtil;
import com.cc.crm.utils.SqlSessionUtil;
import com.cc.crm.utils.UUIDUtil;
import com.cc.crm.vo.PaginationVO;
import com.cc.crm.workbench.dao.CustomerDao;
import com.cc.crm.workbench.dao.TranDao;
import com.cc.crm.workbench.dao.TranHistoryDao;
import com.cc.crm.workbench.domain.Activity;
import com.cc.crm.workbench.domain.Customer;
import com.cc.crm.workbench.domain.Tran;
import com.cc.crm.workbench.domain.TranHistory;
import com.cc.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    public boolean save(Tran t, String customerName) {

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            //添加客户
            int count = customerDao.save(customer);
            if (count != 1 ){
                flag = false;
            }
        }

        //添加交易
        t.setCustomerId(customer.getId());
        int count2 = tranDao.save(t);
        if (count2 != 1){
            flag = false;
        }

        //添加交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(t.getId());
        tranHistory.setStage(t.getStage());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(tranHistory);
        if (count3 != 1){
            flag = false;
        }

        return flag;
    }


    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;

    }


    public List<TranHistory> getTranHistoryByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getTranHistoryByTranId(tranId);
        return thList;

    }


    public boolean changeStage(Tran t) {

        boolean flag = true;

        //改变交易阶段
        int count = tranDao.changeStage(t);
        if (count != 1){
            flag = false;
        }

        //交易阶段改变后生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        int count2 = tranHistoryDao.save(th);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }


    public  Map<String,Object> getCharts() {

        int total = tranDao.getTranTotal();
        List<Map<String,Object>> dataList = tranDao.getTranList();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("dataList",dataList);

        return map;

    }




}
