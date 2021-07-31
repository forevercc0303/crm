package com.cc.crm.workbench.service.impl;

import com.cc.crm.settings.dao.UserDao;
import com.cc.crm.settings.domain.User;
import com.cc.crm.utils.DateTimeUtil;
import com.cc.crm.utils.SqlSessionUtil;
import com.cc.crm.utils.UUIDUtil;
import com.cc.crm.vo.PaginationVO;
import com.cc.crm.workbench.dao.*;
import com.cc.crm.workbench.domain.*;
import com.cc.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    //线索
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao carDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    public boolean save(Clue c) {
        boolean flag = true;
        int count = clueDao.save(c);
        if (count !=1){
            flag = false;
        }

        return flag;
    }


    public PaginationVO<Clue> pageList(Map<String, Object> map) {

        PaginationVO<Clue> vo = new PaginationVO<Clue>();

        int total = clueDao.getTotalByCondition(map);
        List<Clue> dataList = clueDao.getClueDataListByCondition(map);
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }


    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
         return c;

    }


    public Map<String, Object> getUserListAndClue(String id) {

        List<User> uList = userDao.getUserList();

        Clue c  = clueDao.getById(id);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList",uList);
        map.put("c",c);

        return map;

    }


    public boolean update(Clue c) {

        boolean flag = true;

        int count = clueDao.update(c);
        if (count!=1){
            flag = false;
        }
        return flag;

    }


    public boolean delete(String[] ids) {
        boolean flag = true;

        int count1 = clueRemarkDao.getCountByAIds(ids);
        int count2 = clueRemarkDao.deleteByAIds(ids);

        if (count1 != count2){
            flag = false;
        }

        int count3 = clueDao.deletec(ids);
        if (count3 != ids.length){
            flag = false;
        }
        return flag;
    }


    public boolean unbund(String id) {
        boolean flag = true;
        int count = carDao.unbund(id);
        if (count !=1){
            flag = false;
        }

        return flag;
    }


    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for (String aid :aids){

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = carDao.bund(car);
            if (count != 1){
                flag = false;
            }
        }

        return flag;
    }


    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;

        Clue c = clueDao.getById(clueId);
        String company = c.getCompany();
        Customer customer = customerDao.getCustomerByName(company);

        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(c.getAddress());
            customer.setWebsite(c.getWebsite());
            customer.setPhone(c.getPhone());
            customer.setOwner(c.getOwner());
            customer.setNextContactTime(c.getNextContactTime());
            customer.setName(company);
            customer.setDescription(c.getDescription());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(c.getContactSummary());

            int count = customerDao.save(customer);
            if (count != 1){
                flag = false;
            }
        }

        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAddress(c.getAddress());
        contacts.setOwner(c.getOwner());
        contacts.setNextContactTime(c.getNextContactTime());
        contacts.setSource(c.getSource());
        contacts.setMphone(c.getMphone());
        contacts.setFullname(c.getFullname());
        contacts.setJob(c.getJob());
        contacts.setEmail(c.getEmail());
        contacts.setDescription(c.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setContactSummary(c.getContactSummary());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setAppellation(c.getAppellation());
        int count1 = contactsDao.save(contacts);
        if (count1 != 1){
            flag = false;
        }

        List<ClueRemark> clueRemarkList= clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList){
            //取出备注信息
           String noteContent =  clueRemark.getNoteContent();
           //创建客户备注对象
           CustomerRemark customerRemark = new CustomerRemark();
           customerRemark.setNoteContent(noteContent);
           customerRemark.setId(UUIDUtil.getUUID());
           customerRemark.setCreateBy(createBy);
           customerRemark.setCreateTime(createTime);
           customerRemark.setEditFlag("0");
           customerRemark.setCustomerId(customer.getId());
           int count2 = customerRemarkDao.save(customerRemark);
           if (count2 != 1){
               flag = false;
           }

            //创建联系人备注对象
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());
            int count3 = contactsRemarkDao.save(contactsRemark);
            if (count3 != 1){
                flag = false;
            }

        }

        //线索和市场活动的关系 转换为 联系人和市场活动的关系
        List<ClueActivityRelation> clueActivityRelationList = carDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activityId);
            int count4 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count4 != 1){
                flag = false;
            }

        }

        if (t != null){

            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactsId(contacts.getId());
            t.setContactSummary(c.getContactSummary());
            int count5 = tranDao.save(t);
            if (count5 != 1){
                flag = false;
            }

            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            int count6 = tranHistoryDao.save(tranHistory);
            if (count6 != 1){
                flag = false;
            }
        }



        for (ClueRemark clueRemark : clueRemarkList){
            int count7 = clueRemarkDao.delete(clueRemark);
            if (count7 != 1){
                flag = false;
            }
        }

        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            int count8 = carDao.delete(clueActivityRelation);
            if ( count8 != 1){
                flag = false;
            }
        }

        int count9 = clueDao.delete(clueId);
        if ( count9 != 1){
            flag = false;
        }


        return flag;
    }
}
