package com.cc.srttings.test;

import com.cc.crm.utils.DateTimeUtil;
import com.cc.crm.utils.MD5Util;

public class Test1 {
    public static void main(String[] args) {

        /*String str = DateTimeUtil.getSysTime();
        String expireTime = "2022-10-10 10:10:10";
        int count = expireTime.compareTo(str);
        System.out.println(count);*/

        String pwd = "123";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);

    }
}
