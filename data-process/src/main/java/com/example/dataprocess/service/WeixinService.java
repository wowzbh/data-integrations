package com.example.dataprocess.service;

import java.util.HashMap;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.dataprocess.DynamicData.SwitchDataSource;
import com.example.dataprocess.mapper.TestMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeixinService {

    @Autowired
    TestMapper testMapper;
    public List<HashMap<String, Object>> getFieldValue() {

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("001109");
        SwitchDataSource.setDataSource(druidDataSource, druidDataSource.getUrl());
        List<HashMap<String, Object>> res = testMapper.getWxValue();
        SwitchDataSource.setDefaultDataSource();
        return res;
    }
}
