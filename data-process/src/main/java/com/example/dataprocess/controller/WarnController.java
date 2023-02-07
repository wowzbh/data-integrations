package com.example.dataprocess.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.example.dataprocess.domain.WarnInfo;
import com.example.dataprocess.service.WarnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wa")
public class WarnController {
    
    @Autowired
    WarnService warnService;
    
    @PostMapping("/add")
    public Object addWarn(@RequestBody WarnInfo warnInfo ) {

        // warnService.warnInit(warnInfo.getTableName());
        // Iterator iter =  warnInfo.getWarnlist().iterator();
        // while(iter.hasNext()) {
        //     HashMap<String,Object> map = (HashMap<String, Object>) iter.next();
        //     warnService.warnInt((String)map.get("tableName"),(String) map.get("fieldName"),(String) map.get("warnType"), (Integer)map.get("warnValue"));
        // }
        System.out.println(warnInfo);
        String tableName = warnInfo.getTableName();
        LinkedHashMap<String,Object> reqMap = warnInfo.getWarnMap();
        
        String warnNamelist = (String)warnInfo.getWarnName();
        List<String> fieldlist = (List<String>) reqMap.get("fieldName");
        List<String> typeist = (List<String>) reqMap.get("type");
        List<String> valuelist = (List<String>) reqMap.get("value");
        

        LinkedList<Object> reslist = new LinkedList<>();
        for(int i = 0; i < fieldlist.size(); i++) {
            
            warnService.add(tableName, warnNamelist, fieldlist.get(i), typeist.get(i),Float.parseFloat(valuelist.get(i)));
        }
        return "ok";
    }

    @GetMapping("/show")
    public Object showWarn() {
        return warnService.showWarnInfo();
    }

    @GetMapping("/search")
    public Object searchWarn(@RequestParam("") String checkedTable) {
        return warnService.search(checkedTable);
    }

    @GetMapping("/field")
    public Object getField(@RequestParam("tableName")String tableName) {
        return warnService.getfield(tableName);
    }

    @GetMapping("/delete")
    public Object delete(@RequestParam("warnName")String warnName) {
        return warnService.delete(warnName);
    }


}
