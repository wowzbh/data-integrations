package com.example.dataprocess.service;

import java.util.HashMap;
import java.util.List;

import com.example.dataprocess.mapper.BasicMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSourceService {
    @Autowired
    BasicMapper basicMapper;

    public List<HashMap<String, Object>> getAllDbNames() {
        Integer u_id = 10001;
        return basicMapper.getDbConnNames(u_id);
    }

    public void updateDatabase(String nv_db_name) {
        
    }
}
