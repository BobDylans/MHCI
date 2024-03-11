package com.example.mhcidemo.Service;



import com.example.mhcidemo.domain.LoginParam;
import com.example.mhcidemo.domain.Result;
import com.example.mhcidemo.domain.User;

import java.util.List;

public interface UserService {

    // 注册用户方法，接收User对象
    boolean register(User user);

    // 登录方法，接收用户名和密码，返回User对象
    User login(String username, String password);

    Result sendMsg(String phone);

    Result Login(LoginParam loginParam);

    List<User> selectAll();


    Result Logon(LoginParam loginParam);

}


