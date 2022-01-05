package com.example.demo.demo.service;


import com.example.demo.demo.mapper.DetailMapper;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class DetailService<T, Mapper extends DetailMapper<T>> {

    @Autowired(required =false)
    private Mapper mapper;


    public Mapper getMapper() {
        return this.mapper;
    }

    public T getByIdForExportDetail(Long id) {
      return this.mapper.getByIdForExportDetail(id);
    }
}
