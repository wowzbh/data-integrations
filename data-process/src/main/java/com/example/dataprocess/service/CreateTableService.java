package com.example.dataprocess.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.dataprocess.DynamicData.SwitchDataSource;
import com.example.dataprocess.mapper.BasicMapper;
import com.example.dataprocess.mapper.UserMysqlMapper;
import com.example.dataprocess.mapper.UserOracleMapper;
import com.example.dataprocess.thread.CreateTbThread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateTableService {

    @Autowired
    BasicMapper basicMapper;

    @Autowired
    UserOracleMapper oracleMapper;

    @Autowired
    UserMysqlMapper mysqlMapper;

    // , List<String> ck_fields_list
    public void createOracleTable(String tb_name, DruidDataSource userDataSource, String nv_db_name,String db_type)
            throws InterruptedException {

        SwitchDataSource.setDataSource(userDataSource, nv_db_name);
        System.out.println(tb_name);
        List<String> ck_fields_list = getOracleFieldsName(tb_name);
        // 获取字段名和属性
        List<HashMap<String, Object>> fields = oracleMapper.getColumnxName(tb_name, ck_fields_list);
        // 获取字段名和字段值
        List<HashMap<String, Object>> fieldsValueList = oracleMapper.getCheckedFieldsByTableName(tb_name, ck_fields_list);
        // List<HashMap<String, Object>> fieldsValueList = null;
        String tableName;
        Integer u_id = 10001;
        tableName =  u_id.toString()+ "_" + nv_db_name + "_" + tb_name;

        // 连接默认数据源
        SwitchDataSource.setDefaultDataSource();
        // 根据字段名和属性创建表
        Iterator iter = fields.iterator();
		while(iter.hasNext()) {
            HashMap<String, Object> map = (HashMap<String, Object>) iter.next();
            if( map.get("DATA_TYPE").equals("VARCHAR2") || map.get("DATA_TYPE").equals("varchar"))
                map.put("DATA_TYPE","VARCHAR2(255)");
		} 
        System.out.println("创建表：" + tableName);
        basicMapper.createTableByParams(tableName, fields);
        System.out.println("创建完成");

        System.out.println("插入数据：" + fieldsValueList.toString());

        // 多线程插入
        quickInsert(tableName, fieldsValueList, basicMapper);
        // 根据字段名和字段值插入
        // Iterator iter1 = fieldsValueList.iterator();
        // while(iter1.hasNext()) {
        //     HashMap<String, Object> map = (HashMap<String, Object>) iter1.next();
        //     basicMapper.insertSignal(tableName, map);
		// } 
        // basicMapper.insertByData(tableName, fieldsValueList);

        System.out.println("插入数据到 USER_TABLE_INFO");
        // 插入表信息到 USER_TABLE_INFO
        HashMap<String, Object> dMap = new HashMap<>();  
        String fields_name = new String();
        iter = ck_fields_list.iterator();
        if(iter.hasNext())
            fields_name = (String)iter.next();
        while(iter.hasNext()) {
            fields_name += "," + (String)iter.next();
        }
        
        dMap.put("u_id",u_id);  // 插入u_id数据
        dMap.put("tb_name",tb_name); // 插入用户的选择的table名
        dMap.put("nv_tb_name",nv_db_name + "_" + tb_name);
        dMap.put("conn_db_name", nv_db_name); 
        dMap.put("fields_name", fields_name); 
        basicMapper.insertSignal("USER_TABLE_INFO", dMap);
        System.out.println("插入完成");
        
    }

    public void createMysqlTable(String db_name, String tb_name, DruidDataSource userDataSource, String nv_db_name,String db_type)
            throws InterruptedException {

        SwitchDataSource.setDataSource(userDataSource, nv_db_name);
        System.out.println(tb_name);
        List<String> ck_fields_list = getMysqlFieldsName(db_name, tb_name);
        // 获取字段名和属性
        List<HashMap<String, Object>> fields = mysqlMapper.getColumnxName(db_name, tb_name, ck_fields_list);
        // 获取字段名和字段值
        List<HashMap<String, Object>> fieldsValueList = mysqlMapper.getCheckedFieldsByTableName(tb_name, ck_fields_list);
        // List<HashMap<String, Object>> fieldsValueList = null;
        String tableName;
        Integer u_id = 10001;
        tableName =  u_id.toString()+ "_" + nv_db_name + "_" + tb_name;

        // 连接默认数据源
        SwitchDataSource.setDefaultDataSource();
        // 根据字段名和属性创建表
        Iterator iter = fields.iterator();
		while(iter.hasNext()) {
            HashMap<String, Object> map = (HashMap<String, Object>) iter.next();
            System.out.println("map: " + map);
            if( map.get("data_type").equals("VARCHAR2") || map.get("data_type").equals("varchar"))
                map.put("data_type","VARCHAR2(255)");
            else if( map.get("data_type").equals("text"))
                map.put("data_type","CLOB");
            else if( map.get("data_type").equals("decimal") || map.get("data_type").equals("bigint")|| map.get("data_type").equals("int"))
                map.put("data_type","NUMBER");
        } 
        System.out.println("fields" + fields);
        basicMapper.createMySqlTableByParams(tableName, fields);

        // 多线程插入
        quickInsert(tableName, fieldsValueList, basicMapper);

        // 插入表信息到 USER_TABLE_INFO
        HashMap<String, Object> dMap = new HashMap<>();  
        String fields_name = new String();
        iter = ck_fields_list.iterator();
        if(iter.hasNext())
            fields_name = (String)iter.next();
        while(iter.hasNext()) {
            fields_name += "," + (String)iter.next();
        }
        
        dMap.put("u_id",u_id);  // 插入u_id数据
        dMap.put("tb_name",tb_name); // 插入用户的选择的table名
        dMap.put("nv_tb_name",nv_db_name + "_" + tb_name);
        dMap.put("conn_db_name", nv_db_name); 
        dMap.put("fields_name", fields_name); 
        basicMapper.insertSignal("USER_TABLE_INFO", dMap);

        
    }

    // 插入数据库信息到 USER_DATABASE_INFO
    public void insertDatabase(String db_type, String nv_db_name, DruidDataSource userDataSource) {

        int u_id = 10001;

        
        HashMap<String, Object> iMap = new HashMap<>();  
        iMap.put("u_id",u_id);
        iMap.put("db_type",db_type);
        iMap.put("nv_db_name",nv_db_name);
        iMap.put("driverclass",userDataSource.getDriverClassName());
        iMap.put("db_url",userDataSource.getUrl());
        iMap.put("db_username",userDataSource.getUsername());
        iMap.put("db_password",userDataSource.getPassword());
        basicMapper.insertSignal("USER_DATABASE_INFO", iMap);
    }
    // 判断是否可以创建表
    public boolean isCreate(String nv_db_name) {
        if(basicMapper.getDbName(nv_db_name) == null)
            return true;
        else
            return false;
    }


    
    public List<String> getOracleFieldsName(String tableName) {
        return oracleMapper.getColumnName(tableName);
    }
    public List<String> getMysqlFieldsName(String dbName, String tableName) {
        return mysqlMapper.getColumnName(dbName, tableName);
    }


    // 多线程插入数据
    private void quickInsert(String tableName, List<HashMap<String,Object>> list,BasicMapper basicMapper) throws InterruptedException {
        int count = 300;   // 一个线程处理300条数据
        int listSize = list.size();  // 数据集合大小
        int runSize = (listSize/count)+1; // 开启的线程数
        List<HashMap<String,Object>> newlist = null; // 存放每个线程的执行数据
        ExecutorService executor = Executors.newFixedThreadPool(runSize); // 创建一个线程池数量和开启线程的数量一样
        // 创建两个计数器
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(runSize);

        // 循环创建线程
        for(int i = 0; i < runSize; i++) {
            // 计算每个线程执行的数据
            if((i+1) == runSize) {
                int startIndex = (i*count);
                int endIndex = list.size();
                newlist = list.subList(startIndex, endIndex);
            } else {
                int startIndex = (i*count);
                int endIndex = (i+1)*count;
                newlist = list.subList(startIndex, endIndex);
            }
            // 线程类
            CreateTbThread mythead = new CreateTbThread(tableName, newlist, basicMapper, begin, end);
            executor.execute(mythead);
        }
        begin.countDown();
        end.await();

        executor.shutdown();

    }


   
}