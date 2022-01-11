package com.example.demo.demo.util;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface ShardPageMapper{
    <T> List<T> queryForListShardingPage(Map<String, Object> paramMap);
}
