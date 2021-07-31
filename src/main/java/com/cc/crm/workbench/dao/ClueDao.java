package com.cc.crm.workbench.dao;

import com.cc.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);


    Clue getById(String clueId);

    int delete(String clueId);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueDataListByCondition(Map<String, Object> map);

    int update(Clue c);

    int deletec(String[] ids);
}
