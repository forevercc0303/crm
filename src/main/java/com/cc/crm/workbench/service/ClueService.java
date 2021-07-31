package com.cc.crm.workbench.service;

import com.cc.crm.vo.PaginationVO;
import com.cc.crm.workbench.domain.Clue;
import com.cc.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {


    boolean save(Clue c);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue c);

    boolean delete(String[] ids);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);


    boolean convert(String clueId, Tran t, String createBy);

}
