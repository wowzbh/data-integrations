package com.example.dataprocess.service;

import java.util.Iterator;
import java.util.List;

import com.example.dataprocess.mapper.BasicMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteService {
    @Autowired
    BasicMapper basicMapper;

    public void deleteDataSource(String db_name) {
        Integer u_id = 10001;
        List<String> tbNamelist = basicMapper.getdeleteName(db_name);

        // 删除数据源连接信息
        basicMapper.deleteValue("USER_DATABASE_INFO", "nv_db_name", db_name);
        // 删除表信息
        basicMapper.deleteValue("USER_TABLE_INFO", "conn_db_name", db_name);

        Iterator iter = tbNamelist.iterator();
        while(iter.hasNext()) {
            String tbName = (String)iter.next();
            // 删除预警信息
            basicMapper.deleteValue("USER_WARN_INFO", "tb_name", tbName);
            // 删除仪表盘信息
            basicMapper.deleteValue("USER_DASHBOARD_INFO", "tb_name", tbName);

            tbName = u_id.toString() + "_" + tbName;

            // 删除用户表
            basicMapper.deleteTable(tbName);
            
        }
    }
}
