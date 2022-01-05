package com.example.demo.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DetailMapper <T>{
    T getByIdForExportDetail(Long id);
}
