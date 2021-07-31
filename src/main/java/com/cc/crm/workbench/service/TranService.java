package com.cc.crm.workbench.service;

import com.cc.crm.vo.PaginationVO;
import com.cc.crm.workbench.domain.Tran;
import com.cc.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getTranHistoryByTranId(String tranId);

    boolean changeStage(Tran t);


    Map<String,Object> getCharts();


}
