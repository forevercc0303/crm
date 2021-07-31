package com.cc.crm.workbench.web.controller;


import com.cc.crm.settings.domain.User;
import com.cc.crm.settings.service.UserService;
import com.cc.crm.settings.service.impl.UserServiceImpl;
import com.cc.crm.utils.*;
import com.cc.crm.vo.PaginationVO;
import com.cc.crm.workbench.domain.Customer;
import com.cc.crm.workbench.domain.Tran;
import com.cc.crm.workbench.domain.TranHistory;
import com.cc.crm.workbench.service.CustomerService;
import com.cc.crm.workbench.service.TranService;
import com.cc.crm.workbench.service.impl.CustomerServiceImpl;
import com.cc.crm.workbench.service.impl.TranServiceImpl;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到交易控制器");

        String path = req.getServletPath();

        if ("/workbench/transaction/add.do".equals(path)){

            add(req,resp);

        }
        else if ("/workbench/transaction/getCustomerName.do".equals(path)){

            getCustomerName(req,resp);

        }else if ("/workbench/transaction/save.do".equals(path)){

            save(req,resp);

        }else if ("/workbench/transaction/detail.do".equals(path)){

            detail(req,resp);

        }else if ("/workbench/transaction/getTranHistoryByTranId.do".equals(path)){

            getTranHistoryByTranId(req,resp);

        }else if ("/workbench/transaction/changeStage.do".equals(path)){

            changeStage(req,resp);

        }else if ("/workbench/transaction/getCharts.do".equals(path)){

            getCharts(req,resp);

        }else if ("/workbench/transaction/getTranList.do".equals(path)){

            //getTranList(req,resp);

        }
    }



    private void getCharts(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("交易图标");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Map<String,Object> map = (Map<String, Object>) tranService.getCharts();
        PrintJson.printJsonObj(resp,map);

    }

    private void changeStage(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行改变阶段操作");

        String id = req.getParameter("id");
        String stage = req.getParameter("stage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setMoney(money);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = tranService.changeStage(t);

        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success",flag);
        map.put("t",t);
        PrintJson.printJsonObj(resp,map);

    }

    private void getTranHistoryByTranId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据交易ID展现交易历史列表");

        String tranId = req.getParameter("tranId");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList = tranService.getTranHistoryByTranId(tranId);
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");

        for (TranHistory th:thList){

            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }

        PrintJson.printJsonObj(resp,thList);

    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("跳转到交易详细页");

        String id = req.getParameter("id");

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = tranService.detail(id);

        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        req.setAttribute("t",t);
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("执行添加交易操作");

        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String money = req.getParameter("money");
        String name = req.getParameter("name");
        String expectedDate = req.getParameter("expectedDate");
        String customerName = req.getParameter("customerName");
        String stage = req.getParameter("stage");
        String type = req.getParameter("type");
        String source = req.getParameter("source");
        String activityId = req.getParameter("activityId");
        String contactsId = req.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) req.getSession().getAttribute("user")).getName();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTim = req.getParameter("nextContactTim");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTim);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = tranService.save(t,customerName);
        if (flag){
            resp.sendRedirect(req.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得客户名称列表");

        String name = req.getParameter("name");

        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<Customer> cList = customerService.getCustomerName(name);

        PrintJson.printJsonObj(resp,cList);

    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到跳转交易添加页操作");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = userService.getUserList();
        req.setAttribute("uList",uList);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);


    }


}
