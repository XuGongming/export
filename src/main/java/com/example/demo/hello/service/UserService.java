package com.example.demo.hello.service;

import com.example.demo.hello.dto.User;
import com.example.demo.hello.dto.UserVO;

import java.util.List;

public interface UserService {
    List<UserVO> findAll();

    String updateUserName(User user);

    String insert(User user);
}
