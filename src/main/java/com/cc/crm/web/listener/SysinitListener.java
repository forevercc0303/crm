package com.cc.crm.web.listener;

import com.cc.crm.settings.domain.DicValue;
import com.cc.crm.settings.service.DicService;
import com.cc.crm.settings.service.impl.DicServiceImpl;
import com.cc.crm.utils.ServiceFactory;
import com.cc.crm.utils.SqlSessionUtil;
import com.cc.crm.workbench.domain.Clue;
import com.cc.crm.workbench.service.ClueService;
import com.cc.crm.workbench.service.impl.ClueServiceImpl;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysinitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("服务器处理缓存开始");
        ServletContext application = sce.getServletContext();

        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());

       Map<String, List<DicValue>> map = dicService.getAll();
       Set<String> set = map.keySet();
       for (String key : set){
           application.setAttribute(key,map.get(key));
       }

        System.out.println("服务器处理缓存结束");

       //数据字典处理完成后 处理S2P.properties文件
        Map<String,String> pMap = new HashMap<String,String>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key,value);
        }

        //将pMap保存到服务器中
        application.setAttribute("pMap",pMap);


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
