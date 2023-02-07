package com.example.dataprocess.DynamicData;

import com.alibaba.druid.pool.DruidDataSource;

public class SwitchDataSource {
    // 连接指定数据源
    public static void setDataSource(DruidDataSource druidDataSource, String db_name) {
        // 用户存储的数据库的名称 作为 DataSource 的 key 值
        DynamicDataSource.dataSourcesMap.put(db_name,druidDataSource);
        DynamicDataSource.setDataSource(db_name);
        
    }

    
    // 连接默认数据源
    public static void setDefaultDataSource() {

        if(DynamicDataSource.getDataSource() == "default")
            return;
        else if(DynamicDataSource.dataSourcesMap.containsKey("default")) {
            DynamicDataSource.setDataSource("default");
        }
        else {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
            druidDataSource.setUrl("jdbc:oracle:thin:@139.196.101.174:1521:orcl");
            druidDataSource.setUsername("MainDatabaseOfProcess");
            druidDataSource.setPassword("maindatabaseofprocess");

            DynamicDataSource.dataSourcesMap.put("default", druidDataSource);
            DynamicDataSource.setDataSource("default");
        }
        

    }
}
