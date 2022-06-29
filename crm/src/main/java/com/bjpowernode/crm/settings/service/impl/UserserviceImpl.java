package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.settings.service.impl
 * @Author: dushicheng
 * @CreateTime: 2022-06-13  17:40
 * @Description: TODO
 * @Version: 1.0
 */
@Service("userService")
public class UserserviceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers(){return userMapper.selectAllUsers();}
}
