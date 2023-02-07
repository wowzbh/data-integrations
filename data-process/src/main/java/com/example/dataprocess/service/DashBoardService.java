package com.example.dataprocess.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.example.dataprocess.mapper.BasicMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashBoardService {

    @Autowired
    BasicMapper basicMapper;

    // 查询仪表盘名称
    public List<String> getDashName() {
        Integer u_id = 10001;
        return basicMapper.getDashName(u_id);
    }

    // 创建仪表盘
    public int createDash(String dashName) {
        Integer u_id = 10001;
        HashMap<String, Object> dMap = new HashMap<>();
        
        dMap.put("u_id", u_id); // 插入u_id数据
        dMap.put("dash_name", dashName);
        return basicMapper.insertSignal("USER_DASH_NAME", dMap);

    }

    // 删除仪表盘
    public int deleteDash(String dashName) {
        Integer u_id = 10001;
        basicMapper.deleteValue("USER_DASH_NAME", "dash_name", dashName);
        basicMapper.deleteValue("USER_DASHBOARD_INFO", "dash_name", dashName);

        return 1;
    }

    // 获取用户创建的所有表名
    public List<HashMap<String, Object>> getUserTableName() {
        Integer u_id = 10001;
        return basicMapper.getTableNameById(u_id);
    }

    // 获取指定表中的所有字段
    public List<HashMap<String, Object>> getFieldsName(String tableName) {
        Integer u_id = 10001;
        tableName = u_id.toString() + "_" + tableName;
        return basicMapper.getFieldByTableName(tableName);
    }

    // 根据字段名数组获得这些字段的所有值
    public Object getFieldsValue(String tableName, List<String> dimList, List<String> numList) {
        Integer u_id = 10001;
        tableName = u_id.toString() + "_" + tableName;
        HashMap<String, Object> resMap = new LinkedHashMap<>();
        HashMap<String, Object> dimMap = new LinkedHashMap<>();
        HashMap<String, Object> numMap = new LinkedHashMap<>();

        List<Object> fieldValue = null; // 存放字段的数据

        Iterator dimIter = dimList.iterator();
        while (dimIter.hasNext()) {
            String fieldName = (String) dimIter.next();
            fieldValue = basicMapper.getFieldsValue(tableName, fieldName);
            dimMap.put(fieldName, fieldValue);
        }
        Iterator numIter = numList.iterator();
        while (numIter.hasNext()) {
            String fieldName = (String) numIter.next();
            fieldValue = basicMapper.getFieldsValue(tableName, fieldName);
            numMap.put(fieldName, fieldValue);
        }

        resMap.put("dimension", dimMap);
        resMap.put("number", numMap);

        // return basicMapper.getFieldsValue(tableName, dimList);
        return resMap;
    }

    public boolean insertChartOptions(String dashName, String tableName, List<String> dimList, List<String> numList, String chartType) {
        Integer u_id = 10001;
        // tableName = u_id.toString()+ "_" + tableName;

        HashMap<String, Object> imap = new HashMap<>();
        Iterator iter = dimList.iterator();
        String dimension = new String();
        if (iter.hasNext())
            dimension = (String) iter.next();
        while (iter.hasNext()) {
            dimension += "," + (String) iter.next();
        }

        iter = numList.iterator();
        String number = new String();
        if (iter.hasNext())
            number = (String) iter.next();
        while (iter.hasNext()) {
            number += "," + (String) iter.next();
        }

        imap.put("u_id", u_id);
        imap.put("dash_name", dashName);
        imap.put("tb_name", tableName);
        imap.put("dimension", dimension);
        imap.put("number", number);
        imap.put("chart_type", chartType);
        System.out.println(imap);
        basicMapper.insertSignal("USER_DASHBOARD_INFO", imap);
        return true;
    }

    public Object getChartInfo(String dashName) {
        Integer u_id = 10001;
        List<LinkedHashMap<String, Object>> searchList = basicMapper.getChartsInfo(u_id, dashName);
        List<String> dimList = null;
        List<String> numList = null;
        String tableName = new String();
        String chart_type = new String();
        HashMap<String, Object> tmpMap = null;
        List<Object> resList = new LinkedList<>();

        Iterator iter = searchList.iterator();

        while (iter.hasNext()) {
            tmpMap = (HashMap<String, Object>) iter.next();
            dimList = Arrays.asList(((String) tmpMap.get("dimension")).split(","));
            numList = Arrays.asList(((String) tmpMap.get("number")).split(","));
            tableName = (String) tmpMap.get("tb_name");
            chart_type = (String) tmpMap.get("chart_type");
            tmpMap = (HashMap<String, Object>) getFieldsValue(tableName, dimList, numList);
            tmpMap.put("type", chart_type);
            resList.add(tmpMap);
        }
        return resList;
    }

    public List<Object> getyuceFields(String tableName, String fieldName) {
        Integer u_id = 10001;
        tableName = u_id.toString() + "_" + tableName;
        return basicMapper.getFieldsValue(tableName, fieldName);
    }
}
