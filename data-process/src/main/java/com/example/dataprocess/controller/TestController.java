package com.example.dataprocess.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.dataprocess.domain.CreateTableInfo;
import com.example.dataprocess.domain.Test;
import com.example.dataprocess.domain.WarnInfo;
import com.example.dataprocess.mapper.TestMulti;
import com.example.dataprocess.service.MultiTableService;
import com.example.dataprocess.service.PyService;
import com.example.dataprocess.service.WeixinService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    MultiTableService multiService;

    @Autowired
    PyService pyService;

    @Autowired
    TestMulti testService;

    @Autowired
    WeixinService wxService;
    @PostMapping("/test")
    public Object test(@RequestBody Test t) {
        Iterator iter_table = t.getSync_tables().iterator();
        while (iter_table.hasNext()) {

            HashMap<String, String> map = (HashMap<String, String>) iter_table.next();
            String tableName = map.get("TABLE_NAME");
            System.out.println(tableName);
        }
        return t;
    }

    @GetMapping("/status")
    public Object testS() {
        return new ResponseEntity<Object>("123", HttpStatus.OK);
    }

    @PostMapping("/multi")
    public Object testM(@RequestBody LinkedHashMap<String, Object> map) {
        System.out.println(map);
        LinkedHashMap<String, Object> test = (LinkedHashMap<String, Object>) map.get("map");
        System.out.println(test);
        // List<LinkedHashMap<String, Object>> reslist = new LinkedList<>();
        // LinkedHashMap<String, Object> lmap = new LinkedHashMap<>();
        // String lstr = new String();
        // multiService.multiTable(test, reslist, lstr);
        // System.out.println(testService.test(reslist));

        // System.out.println(reslist);
        return map;
    }

    @GetMapping("/py")
    public void py() {
        // try {
        //     pyService.insert();
        // } catch (IOException | InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

    @PostMapping("/dd")
    public Object DD(@RequestBody String str) {
        System.out.println(str);
        return str;
    }

    @PostMapping("/add")
    public Object addWarn(@RequestBody List<WarnInfo> warnInfo ) {
        System.out.println(warnInfo);

        return warnInfo;
    }
    
    @PostMapping("/create")
    public Object CreateTable(@RequestBody CreateTableInfo createTableInfo) throws InterruptedException {
        System.out.println(createTableInfo);
        System.out.println(createTableInfo.getDb_type().equals("1"));
        return createTableInfo;
        
    }

    @GetMapping("/wx")
    public Object WeixinTest(){

        return wxService.getFieldValue();
    }

    
}
