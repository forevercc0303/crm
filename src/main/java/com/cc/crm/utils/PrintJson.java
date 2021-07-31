package com.cc.crm.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrintJson {
    //将boolean值解析为json串
    public static void printJsonFlag(HttpServletResponse response,boolean flag) {

        Map<String,Boolean> map = new HashMap<String,Boolean>();
        map.put("success",flag);

        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(map);
            response.getWriter().print(json);
        }catch (JsonGenerationException e){
            e.printStackTrace();
        }catch (JsonMappingException e ){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //将对象解析为json串
    public static void printJsonObj(HttpServletResponse response,Object object){

        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(object);
            response.getWriter().print(json);
        }catch (JsonGenerationException e){
            e.printStackTrace();
        }catch (JsonMappingException e ){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
