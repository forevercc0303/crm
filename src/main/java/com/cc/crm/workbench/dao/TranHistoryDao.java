package com.cc.crm.workbench.dao;

import com.cc.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<TranHistory> getTranHistoryByTranId(String tranId);
}
