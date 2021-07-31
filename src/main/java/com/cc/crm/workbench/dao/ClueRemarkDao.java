package com.cc.crm.workbench.dao;

import com.cc.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);

    int getCountByAIds(String[] ids);

    int deleteByAIds(String[] ids);
}
