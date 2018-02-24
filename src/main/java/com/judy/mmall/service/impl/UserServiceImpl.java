package com.judy.mmall.service.impl;

import com.judy.mmall.commom.Const;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.dao.UserMapper;
import com.judy.mmall.pojo.User;
import com.judy.mmall.service.IUserService;
import com.judy.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int userCount = userMapper.checkUsername(username);
        if (userCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String MD5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, MD5Password);
        if (null == user) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在");
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }
}
