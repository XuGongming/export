package com.example.demo.demo.mapper;

import com.example.demo.demo.dto.User;
import com.example.demo.demo.dto.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Component
public interface UserMapper extends DetailMapper<UserVO>{
    List<UserVO> findAll();

    void updateUserName(User user);

    void insertUser(User user);
}
