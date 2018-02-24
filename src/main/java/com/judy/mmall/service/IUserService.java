package com.judy.mmall.service;

import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);
}
