package com.judy.mmall.service.impl;

import com.judy.mmall.commom.Const;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.commom.TokenCache;
import com.judy.mmall.dao.UserMapper;
import com.judy.mmall.pojo.User;
import com.judy.mmall.service.IUserService;
import com.judy.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case Const.USERNAME:
                    if (userMapper.checkUsername(str) > 0) {
                        return ServerResponse.createByErrorMessage("用户名已存在");
                    }
                    break;
                case Const.EMAIL:
                    if (userMapper.checkEmail(str) > 0) {
                        return ServerResponse.createByErrorMessage("email已注册");
                    }
                    break;
            }
            return ServerResponse.createBySuccessMessage("校验成功");
        } else {
            return ServerResponse.createByErrorMessage("参数有误");
        }

    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        } else {
            return ServerResponse.createByErrorMessage("找回密码问题不存在");
        }
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        } else {
            return ServerResponse.createByErrorMessage("答案错误");
        }

    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("没有传递token");
        }

        ServerResponse<String> validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String MD5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username, MD5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateUserInformation(User user) {
        int resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("该Email已被注册");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        } else {
            return ServerResponse.createByErrorMessage("更新个人信息失败");
        }
    }

    @Override
    public ServerResponse<User> getUserInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (null == user) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
