package com.example.demo.demo.service;

import com.example.demo.demo.dto.User;
import com.example.demo.demo.dto.UserVO;

import java.util.List;

public interface UserService {
    List<UserVO> findAll();

    String updateUserName(User user);

    String insert(User user);
}
