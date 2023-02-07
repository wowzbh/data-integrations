package com.example.dataprocess.controller;

import java.util.HashMap;
import java.util.List;

import com.example.dataprocess.service.DataSourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dspage")
public class DataSourceController {

    @Autowired
    DataSourceService dataSourceService;


    @GetMapping("/names")
    public List<HashMap<String, Object>> getDbAllName() {
        // System.out.println(dataSourceService.getAllDbNames());
        List<HashMap<String, Object>> reslist =  dataSourceService.getAllDbNames();
        return reslist;
    }

    @GetMapping("/update")
    public Object updateDbConn(@RequestParam("db_name") String nv_db_name ) {
        return 1;
    }
}
