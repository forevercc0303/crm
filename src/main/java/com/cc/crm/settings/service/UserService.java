package com.cc.crm.settings.service;

import com.cc.crm.exception.LoginException;
import com.cc.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();

}
