package com.example.dataprocess.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.dataprocess.mapper.BasicMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarnService {
    @Autowired
    BasicMapper basicMapper;


    public int warnInit(String tableName) {
        Integer u_id = 10001;
        tableName = u_id.toString() + "_" + tableName;
        return basicMapper.initWarnInfo(tableName);
    }

    public List<String> getfield(String tableName) {
        Integer u_id = 10001;
        tableName = u_id.toString() + "_" + tableName;
        return basicMapper.getFieldList(tableName);
    }

    public int add(String tableName, String warnName, String fieldName, String warnType, Float warnValue) {

        Integer u_id = 10001;

        String active = "未激活";
        switch (warnType) {
            case "等于":
                warnType = "=";
                break;
            case "不等于":
                warnType = "!=";
                break;
            case "大于":
                warnType = ">";
                break;
            case "小于":
                warnType = "<";
                break;
            case "大于等于":
                warnType = ">=";
                break;
            case "小于等于":
                warnType = "<=";
                break;
        }

        HashMap<String, Object> dMap = new HashMap<>();

        dMap.put("u_id", u_id); // 插入u_id数据
        dMap.put("warn_name", warnName); // 插入用户的选择的table名
        dMap.put("tb_name", tableName);
        dMap.put("field_name", fieldName);
        dMap.put("warn_type", warnType);
        dMap.put("warn_value", warnValue);
        tableName = u_id.toString() + "_" + tableName;
        LinkedList list = basicMapper.getWarn(tableName, fieldName, warnType, warnValue);
        System.out.println(list);
        if (list != null || !list.isEmpty())
            active = "激活";
        else
            active = "未激活";
        dMap.put("active", active);

        return basicMapper.insertSignal("USER_WARN_INFO", dMap);
    }

    public LinkedList<Object> search(String tableName) {
        Integer u_id = 10001;
        LinkedList<LinkedHashMap<String, Object>> rlist = basicMapper.getWarnByTb(u_id, tableName);
        LinkedList<Object> reslist = new LinkedList<>();
        Iterator iter = rlist.iterator();
        while (iter.hasNext()) {
            LinkedHashMap<String, Object> tmap = (LinkedHashMap<String, Object>) iter.next();
            tableName = u_id.toString() + "_" + tableName;
            String fieldName = (String) tmap.get("field_name");
            String warnType = (String) tmap.get("warn_type");
            Integer warnValue = Integer.valueOf(tmap.get("warn_value").toString());
            reslist.addAll(basicMapper.getWarn(tableName, fieldName, warnType, warnValue));
        }

        return reslist;
    }

    public LinkedList<Object> showWarnInfo() {
        Integer u_id = 10001;
        LinkedList reslist = new LinkedList<>();
        LinkedList<LinkedHashMap<String, Object>> rlist = basicMapper.getWarnById(u_id);
        Iterator iter = rlist.iterator();
        while (iter.hasNext()) {
            LinkedHashMap<String, Object> rmap = (LinkedHashMap<String, Object>) iter.next();
            LinkedHashMap<String, Object> tmap = new LinkedHashMap<>();
            tmap.put("warnName", (String) rmap.get("warn_name"));
            tmap.put("active", rmap.get("active"));
            tmap.put("field",rmap.get("field_name"));
            String warnType = (String) rmap.get("warn_type");
            tmap.put("type",warnType);
            tmap.put("value",rmap.get("warn_value"));
            tmap.put("tableName",rmap.get("tb_name"));
            reslist.add(tmap);
        }
        return reslist;
    }
    public int delete(String warnName) {
        
        return basicMapper.deleteValue("USER_WARN_INFO","warn_name",warnName);
    }
}
