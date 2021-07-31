package com.cc.crm.settings.dao;

import com.cc.crm.settings.domain.DicType;
import com.cc.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getListByCode(String code);
}
