package com.example.dataprocess.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.dataprocess.domain.CreateTableInfo;
import com.example.dataprocess.domain.DataBaseInfo;
import com.example.dataprocess.service.ConnDatabaseService;
import com.example.dataprocess.service.CreateTableService;
import com.example.dataprocess.service.DataSourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ds")
public class ConnDatabaseController {

    @Autowired
    ConnDatabaseService connService;

    @Autowired
    CreateTableService createService;

    @Autowired
    DataSourceService dataSourceService;
    // @RequestMapping(value="/conn",method=RequestMethod.POST)
    // @ResponseBody

    @PostMapping("/conn")
    // @ResponseBody
    public List<HashMap<String, Object>> ConnectDatabase(@RequestBody DataBaseInfo dataBaseInfo) {
        // driver-class-name: oracle.jdbc.driver.OracleDriver
        // url: jdbc:oracle:thin:@47.102.146.116:1521:orcl

        System.out.println(dataBaseInfo);
        String driverClass = new String();
        String url = new String();

        if (dataBaseInfo.getDb_type().equals("1")) {
            driverClass = "oracle.jdbc.driver.OracleDriver";
            url = "jdbc:oracle:thin:@" + dataBaseInfo.getHost() + ":" + dataBaseInfo.getPort() + ":"
                    + dataBaseInfo.getDatabase();

            DruidDataSource userDataSource = new DruidDataSource();
            userDataSource.setDriverClassName(driverClass);
            userDataSource.setUrl(url);
            userDataSource.setUsername(dataBaseInfo.getUsername());
            userDataSource.setPassword(dataBaseInfo.getPassword());
            System.out.println("url: "+url);
            return connService.getOracleTableName(userDataSource);
        } else if (dataBaseInfo.getDb_type().equals("2")) {
            driverClass = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://" + dataBaseInfo.getHost() + ":" + dataBaseInfo.getPort() + "/"
                    + dataBaseInfo.getDatabase() + "?serverTimezone=GMT%2B8";
            DruidDataSource userDataSource = new DruidDataSource();

            userDataSource.setDriverClassName(driverClass);
            userDataSource.setUrl(url);
            userDataSource.setUsername(dataBaseInfo.getUsername());
            userDataSource.setPassword(dataBaseInfo.getPassword());
            return connService.getMySqlTableName(userDataSource, dataBaseInfo.getDatabase());
        }
        return null;
    }

    @PostMapping("/create")
    public Object CreateTable(@RequestBody CreateTableInfo createTableInfo) throws InterruptedException {
        if (!createService.isCreate(createTableInfo.getName()))
            return "创建失败";

        String driverClass = new String();
        String url = new String();

        if (createTableInfo.getDb_type().equals("1")) {
            driverClass = "oracle.jdbc.driver.OracleDriver";
            url = "jdbc:oracle:thin:@" + createTableInfo.getHost() + ":" + createTableInfo.getPort() + ":"
                    + createTableInfo.getDatabase();
            DruidDataSource userDataSource = new DruidDataSource();

            userDataSource.setDriverClassName(driverClass);
            userDataSource.setUrl(url);
            userDataSource.setUsername(createTableInfo.getUsername());
            userDataSource.setPassword(createTableInfo.getPassword());
            
            createService.insertDatabase(createTableInfo.getDb_type(), createTableInfo.getName(), userDataSource);
            Iterator iter_table = createTableInfo.getCheck_tables().iterator();
            while (iter_table.hasNext()) {

                HashMap<String, String> map = (HashMap<String, String>) iter_table.next();
                String tableName = map.get("TABLE_NAME");
                createService.createOracleTable(tableName, userDataSource, createTableInfo.getName(),
                        createTableInfo.getDb_type());
            }

        } else if(createTableInfo.getDb_type().equals("2")) {
            driverClass = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://" + createTableInfo.getHost() + ":" + createTableInfo.getPort() + "/"
                    + createTableInfo.getDatabase() + "?serverTimezone=GMT%2B8";
            DruidDataSource userDataSource = new DruidDataSource();

            userDataSource.setDriverClassName(driverClass);
            userDataSource.setUrl(url);
            userDataSource.setUsername(createTableInfo.getUsername());
            userDataSource.setPassword(createTableInfo.getPassword());

            createService.insertDatabase(createTableInfo.getDb_type(), createTableInfo.getName(), userDataSource);
            Iterator iter_table = createTableInfo.getCheck_tables().iterator();
            while (iter_table.hasNext()) {

                HashMap<String, String> map = (HashMap<String, String>) iter_table.next();
                String tableName = map.get("TABLE_NAME");
                createService.createMysqlTable(createTableInfo.getDatabase(), tableName, userDataSource, createTableInfo.getName(),
                        createTableInfo.getDb_type());
            }
            
        }

        return dataSourceService.getAllDbNames();

    }
}
