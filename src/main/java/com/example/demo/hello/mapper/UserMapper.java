package com.example.demo.hello.mapper;

import com.example.demo.hello.dto.User;
import com.example.demo.hello.dto.UserVO;

import java.util.List;

public interface UserMapper {
    List<UserVO> findAll();

    void updateUserName(User user);

    void insertUser(User user);
}
