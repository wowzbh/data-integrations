package com.example.dataprocess.service;

import java.util.HashMap;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.dataprocess.DynamicData.SwitchDataSource;
import com.example.dataprocess.mapper.UserMysqlMapper;
import com.example.dataprocess.mapper.UserOracleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnDatabaseService {

    @Autowired
    private UserOracleMapper oracleMapper;

    @Autowired
    private UserMysqlMapper mysqlMapper;

    // 获取Oracle用户数据库中的所有表名
    public List<HashMap<String, Object>> getOracleTableName(DruidDataSource druidDataSource){
        SwitchDataSource.setDataSource(druidDataSource,druidDataSource.getUrl());
        return oracleMapper.getOracleTableNames();
    }

    // 获取MySql用户数据库中的所有表名
    public List<HashMap<String, Object>> getMySqlTableName(DruidDataSource druidDataSource, String dn_name){
        SwitchDataSource.setDataSource(druidDataSource,druidDataSource.getUrl());
        return mysqlMapper.getTableNames(dn_name);
    }
    // 查询指定表的所有数据
    public List<HashMap<String, Object>> getAllData(String tableName){
        return oracleMapper.getAllByTableName(tableName);
    }



    
}
