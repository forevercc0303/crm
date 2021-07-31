package com.cc.crm.workbench.web.controller;

import com.cc.crm.settings.domain.User;
import com.cc.crm.settings.service.UserService;
import com.cc.crm.settings.service.impl.UserServiceImpl;
import com.cc.crm.utils.*;
import com.cc.crm.vo.PaginationVO;
import com.cc.crm.workbench.domain.Activity;
import com.cc.crm.workbench.domain.ActivityRemark;
import com.cc.crm.workbench.service.ActivityService;
import com.cc.crm.workbench.service.impl.ActivityServiceImpl;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到市场活动控制器");

        String path = req.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(req,resp);

        }
        else if ("/workbench/activity/save.do".equals(path)){

            save(req,resp);
        }
        else if ("/workbench/activity/pageList.do".equals(path)){

            pageList(req,resp);
        }else if ("/workbench/activity/delete.do".equals(path)){

            delete(req,resp);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){

            getUserListAndActivity(req,resp);
        }else if ("/workbench/activity/update.do".equals(path)){

            update(req,resp);
        }else if ("/workbench/activity/detail.do".equals(path)){

            detail(req,resp);
        }else if ("/workbench/activity/getRemarkListByAid.do".equals(path)){

            getRemarkListByAid(req,resp);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){

            deleteRemark(req,resp);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){

            saveRemark(req,resp);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){

            updateRemark(req,resp);
        }


    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("更新备注操作");

        String id = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        boolean flag =  activityService.updateRemark(ar);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(resp,map);

    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("添加备注");

        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();

        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.saveRemark(ar);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(resp,map);


    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("删除备注信息操作");

        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.deleteRemark(id);

        PrintJson.printJsonFlag(resp,flag);

    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("根据市场活动id，取得备注信息");
        String activityId = req.getParameter("activityId");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> arList = activityService.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(resp,arList);


    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("跳转到详细信息页面");
        String id = req.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = activityService.detail(id);

        req.setAttribute("a",a);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);


    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场更新操作");

        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //修改当前时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人，当前登录用户
        String editBy = ((User) req.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditBy(editBy);
        a.setEditTime(editTime);

        ActivityService activityService = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl());

        boolean flag = activityService.update(a);

        PrintJson.printJsonFlag(resp,flag);


    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询用户信息列表根据市场活动id查询单条记录操作");

        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
            controller调用service，前端需要什么，就返回什么。
            uList、a
        */
        Map<String,Object> map = activityService.getUserListAndActivity(id);

        PrintJson.printJsonObj(resp,map);


    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场活动删除操作");
        String ids[] = req.getParameterValues("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.delete(ids);
         PrintJson.printJsonFlag(resp,flag);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入到查询市场活动信息列表操作（结合条件+分页）");

        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        String pageNoStr = req.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map =  new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVO<Activity> vo = activityService.pageList(map);
        PrintJson.printJsonObj(resp,vo);

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("执行市场添加操作");

        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //创建当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy = ((User) req.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl());

        boolean flag = activityService.save(a);

        PrintJson.printJsonFlag(resp,flag);


    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> users = userService.getUserList();

        PrintJson.printJsonObj(resp,users);


    }

}
