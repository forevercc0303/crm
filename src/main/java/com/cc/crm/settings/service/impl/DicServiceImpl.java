package com.cc.crm.settings.service.impl;

import com.cc.crm.settings.dao.DicTypeDao;
import com.cc.crm.settings.dao.DicValueDao;
import com.cc.crm.settings.domain.DicType;
import com.cc.crm.settings.domain.DicValue;
import com.cc.crm.settings.service.DicService;
import com.cc.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<String,List<DicValue>>();
        List<DicType> dtList = dicTypeDao.getTypeList();

        for (DicType dt : dtList){

            String code = dt.getCode();

            List<DicValue> dcList = dicValueDao.getListByCode(code);

            map.put(code,dcList);
        }


        return map;
    }
}
