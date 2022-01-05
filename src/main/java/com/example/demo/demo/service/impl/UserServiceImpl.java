package com.example.demo.demo.service.impl;

import com.example.demo.demo.dto.User;
import com.example.demo.demo.dto.UserVO;
import com.example.demo.demo.mapper.UserMapper;
import com.example.demo.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserVO> findAll() {
        List<UserVO> list = userMapper.findAll();
        return list;
    }

    @Override
    public String updateUserName(User user) {
        userMapper.updateUserName(user);
        return "success";
    }

    @Override
    public String insert(User user) {
        userMapper.insertUser(user);
        return "success";
    }
}
