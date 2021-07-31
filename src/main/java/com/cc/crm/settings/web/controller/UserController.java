package com.cc.crm.settings.web.controller;

import com.cc.crm.settings.domain.User;
import com.cc.crm.settings.service.UserService;
import com.cc.crm.settings.service.impl.UserServiceImpl;
import com.cc.crm.utils.MD5Util;
import com.cc.crm.utils.PrintJson;
import com.cc.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到用户控制器");

        String path = req.getServletPath();

        if ("/settings/user/login.do".equals(path)){
            login(req,resp);

        }
        else if ("/settings/user/xx.do".equals(path)){

        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("进入验证登录");
        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");
        //将密码转化为明文
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器IP地址
        String ip = req.getRemoteAddr();
        System.out.println("--------------------------ip:"+ip);

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{

            User user = userService.login(loginAct,loginPwd,ip);
            req.getSession().setAttribute("user",user);

            PrintJson.printJsonFlag(resp,true);
        }catch (Exception e){
            e.printStackTrace();

            String msg = e.getMessage();

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(resp,map);
        }
    }
}
