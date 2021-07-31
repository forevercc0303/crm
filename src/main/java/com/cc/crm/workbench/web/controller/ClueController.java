package com.cc.crm.workbench.web.controller;

import com.cc.crm.settings.domain.User;
import com.cc.crm.settings.service.UserService;
import com.cc.crm.settings.service.impl.UserServiceImpl;
import com.cc.crm.utils.*;
import com.cc.crm.vo.PaginationVO;
import com.cc.crm.workbench.domain.Activity;
import com.cc.crm.workbench.domain.Clue;
import com.cc.crm.workbench.domain.Tran;
import com.cc.crm.workbench.service.ActivityService;
import com.cc.crm.workbench.service.ClueService;
import com.cc.crm.workbench.service.impl.ActivityServiceImpl;
import com.cc.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = req.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)){

            getUserList(req,resp);

        }
        else if ("/workbench/clue/save.do".equals(path)){

            save(req,resp);

        }else if ("/workbench/clue/pageList.do".equals(path)){

            pageList(req,resp);

        }else if ("/workbench/clue/detail.do".equals(path)){

            detail(req,resp);

        }else if ("/workbench/clue/getUserListAndClue.do".equals(path)){

            getUserListAndClue(req,resp);

        }else if ("/workbench/clue/update.do".equals(path)){

            update(req,resp);

        }else if ("/workbench/clue/delete.do".equals(path)){

            delete(req,resp);

        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){

            getActivityListByClueId(req,resp);

        }else if ("/workbench/clue/unbund.do".equals(path)){

            unbund(req,resp);

        }else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){

            getActivityListByNameAndNotByClueId(req,resp);

        }else if ("/workbench/clue/bund.do".equals(path)){

            bund(req,resp);

        }else if ("/workbench/clue/getActivityListByName.do".equals(path)){

            getActivityListByName(req,resp);

        }else if ("/workbench/clue/convert.do".equals(path)){

            convert(req,resp);

        }
    }

    public void convert(HttpServletRequest req, HttpServletResponse resp) throws  IOException{

        System.out.println("根据线索Id执行线索转换");

        String clueId = req.getParameter("clueId");
        //接收是否需要创建交易的标记
        String flag = req.getParameter("flag");

        Tran t = null;
        String createBy = ((User) req.getSession().getAttribute("user")).getName();

        if ("a".equals(flag)){
            t = new Tran();
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setActivityId(activityId);
            t.setMoney(money);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setId(id);

        }

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag1 = clueService.convert(clueId,t,createBy);

        if (flag1){
            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
        }

    }
    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("查询市场活动列表(根据名称模糊查)");

        String aname = req.getParameter("aname");


        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = activityService.getActivityListByName(aname);
        PrintJson.printJsonObj(resp,aList);

    }

    private void bund(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("添加线索关联市场活动");

        String cid = req.getParameter("cid");
        String aids[] = req.getParameterValues("aid");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = clueService.bund(cid,aids);
        PrintJson.printJsonFlag(resp,flag);

    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("查询市场活动列表(根据名称模糊查+排除掉已经关联指定线索的列表)");

        String aname = req.getParameter("aname");
        String clueId = req.getParameter("clueId");

        Map<String,String> map = new HashMap<String,String>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(resp,aList);

    }

    private void unbund(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("线索活动列表关联解除操作");
        String id = req.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.unbund(id);
        PrintJson.printJsonFlag(resp,flag);

    }

    private void getActivityListByClueId(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据线索id查询关联的线索活动列表");

        String clueId = req.getParameter("clueId");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = activityService.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(resp,aList);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行线索删除操作");

        String ids[] = req.getParameterValues("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = clueService.delete(ids);
        PrintJson.printJsonFlag(resp,flag);

    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行线索更新操作");

        String id = req.getParameter("id");
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");
        //修改当前时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人，当前登录用户
        String editBy = ((User) req.getSession().getAttribute("user")).getName();

       Clue c = new Clue();

        c.setId(id);
        c.setOwner(owner);
        c.setDescription(description);
        c.setAddress(address);
        c.setAppellation(appellation);
        c.setCompany(company);
        c.setContactSummary(contactSummary);
        c.setFullname(fullname);
        c.setPhone(phone);
        c.setEmail(email);
        c.setWebsite(website);
        c.setJob(job);
        c.setMphone(mphone);
        c.setNextContactTime(nextContactTime);
        c.setSource(source);
        c.setState(state);
        c.setEditBy(editBy);
        c.setEditTime(editTime);

        ClueService clueService = (ClueService) ServiceFactory.getService( new ClueServiceImpl());

        boolean flag = clueService.update(c);

        PrintJson.printJsonFlag(resp,flag);

    }

    private void getUserListAndClue(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询用户信息列表根据线索id查询单条记录操作");

        String id = req.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*
            controller调用service，前端需要什么，就返回什么。
            uList、c
        */
        Map<String,Object> map = clueService.getUserListAndClue(id);

        PrintJson.printJsonObj(resp,map);

    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService( new ClueServiceImpl());
        Clue c = clueService.detail(id);
        req.setAttribute("c",c);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);

    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询线索信息列表操作（结合条件+分页）");

        String fullname = req.getParameter("fullname");
        String owner = req.getParameter("fullname");
        String company = req.getParameter("company");
        String phone = req.getParameter("phone");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");


        String pageNoStr = req.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map =  new HashMap<String,Object>();
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("state",state);
        map.put("source",source);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);

        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);


        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Clue> vo = clueService.pageList(map);
        PrintJson.printJsonObj(resp,vo);

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");


        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) req.getSession().getAttribute("user")).getName();


        Clue c = new Clue();

        c.setId(id);
        c.setOwner(owner);
        c.setDescription(description);
        c.setAddress(address);
        c.setAppellation(appellation);
        c.setCompany(company);
        c.setContactSummary(contactSummary);
        c.setFullname(fullname);
        c.setPhone(phone);
        c.setEmail(email);
        c.setWebsite(website);
        c.setJob(job);
        c.setMphone(mphone);
        c.setNextContactTime(nextContactTime);
        c.setSource(source);
        c.setState(state);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);

        ClueService clueService = (ClueService) ServiceFactory.getService( new ClueServiceImpl());

        boolean flag = clueService.save(c);

        PrintJson.printJsonFlag(resp,flag);

    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> users = userService.getUserList();

        PrintJson.printJsonObj(resp,users);
    }
}
