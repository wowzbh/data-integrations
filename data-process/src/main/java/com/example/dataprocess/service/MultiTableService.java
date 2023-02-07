package com.example.dataprocess.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.dataprocess.mapper.BasicMapper;
import com.example.dataprocess.mapper.UserOracleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MultiTableService {

    @Autowired
    UserOracleMapper oracleMapper;

    @Autowired
    BasicMapper basicMapper;
    
    public void multiTable(LinkedHashMap<String, Object> map , List<LinkedHashMap<String, Object>> reslist, String leftName) {
        // System.out.println((LinkedHashMap<String, Object>)map.get("children"));
       
        System.out.println((String)map.get("name"));
        if(leftName.isEmpty())
            leftName = (String)map.get("name");
        else {
            LinkedHashMap<String, Object> lmap = new LinkedHashMap<>();
            lmap.put("func", (String)map.get("addfunction"));
            lmap.put("rightName", (String) map.get("name"));
            lmap.put("leftName", leftName);
            lmap.put("rightField", (String)map.get("rightform"));
            lmap.put("leftField", (String)map.get("leftform"));
            reslist.add(lmap);
            leftName = (String)map.get("name");
        }
        if((Object)map.get("children") == null)
            return ;
        List<LinkedHashMap<String, Object>> list = new LinkedList<LinkedHashMap<String, Object>>();
        if(map.get("children") != null){
            list = (List<LinkedHashMap<String, Object>>) map.get("children");
        }
        Iterator iter = list.iterator();
        LinkedHashMap<String, Object> tmpmap = null;
        while(iter.hasNext()) {
            tmpmap = (LinkedHashMap<String, Object>) iter.next();
            multiTable(tmpmap,reslist, leftName);
        }
        
    }

    

    public Object create(List<LinkedHashMap<String, Object>> reslist, String tbName) {
        
        Iterator iter = reslist.iterator();
        String resSql = new String();
        Integer tmpId = 1;
        String sql1 = new String();
        String sql2 = new String();
        Integer u_id = 10001;
        if(iter.hasNext()) {
            LinkedHashMap<String, Object> tmap = (LinkedHashMap<String, Object>) iter.next();
            sql1 = genString(10001, (String)tmap.get("leftName"));
            sql2 = genString(10001, (String)tmap.get("rightName"));
            String leftField = (String) tmap.get("leftField");
            String rightField = (String) tmap.get("rightField");
            String tmpName = tmpId.toString();
            resSql = "select * from (" + sql1 + ") \"" + tmpName + "_1\" "
            + (String) tmap.get("func") + 
            " (" + sql2 + ") \"" + tmpName  + 
            "_2\" on \"" + tmpName + "_1\".\"" + rightField + "_"+ tmap.get("leftName") + "\""
            + "=\"" + tmpName + "_2\".\"" + leftField + "_"+ tmap.get("rightName") + "\"";
            tmpId++;
        }

        // select * from sql1 tmpName1 left join sql2 tmpName2 on ...
        while(iter.hasNext()) {
            sql1 = resSql;
            LinkedHashMap<String, Object> tmap = (LinkedHashMap<String, Object>) iter.next();
            sql2 = genString(10001, (String)tmap.get("rightName"));
            String tmpName = tmpId.toString();
            String leftField = (String) tmap.get("leftField");
            String rightField = (String) tmap.get("rightField");
            resSql = "select * from (" + sql1 + ") \"" + tmpName + "_1\" "
                    + (String) tmap.get("func") + 
                    " (" + sql2 + ") \"" + tmpName + 
                    "_2\" on \"" + tmpName + "_1\".\"" + rightField + "_" + tmap.get("leftName") + "\""
                    + "=\"" + tmpName + "_2\".\"" + leftField + "_"+ tmap.get("rightName") + "\"";
            
            tmpId++;
        } 

        System.out.println(resSql);

        oracleMapper.easyCreate(u_id.toString() + "_" + tbName, resSql);

        HashMap<String, Object> dMap = new HashMap<>();  
        
        dMap.put("u_id",u_id);  // 插入u_id数据
        dMap.put("tb_name",tbName); // 插入用户的选择的table名
        dMap.put("nv_tb_name",tbName);
        
        basicMapper.insertSignal("USER_TABLE_INFO", dMap);
        return resSql;

    }
    private String genString(Integer id, String tableName) {
        String sql = "select ";
        String tmpstr = new String();
        List<String> fieldName = oracleMapper.getColumnName(id.toString() + "_" +tableName);
        Iterator iter = fieldName.iterator();

        if(iter.hasNext()) {
            String tmp = (String)iter.next();
            tmpstr = "\"" + tmp + "\"" + " \"" + tmp + "_" + tableName + "\" ";
            sql += tmpstr;
        }
        while(iter.hasNext()) {
            String tmp = (String)iter.next();
            tmpstr = ",\"" + tmp + "\"" + " \"" + tmp + "_" + tableName + "\" ";
            sql += tmpstr;
        }

        sql += " from " + "\"" + id.toString() + "_"  + tableName+ "\"";
        return sql;
    }
    // 获取指定表中的所有字段
    public HashMap<String, Object> getFieldsName(String leftTable, String rightTable) {
        Integer u_id = 10001;
        leftTable = u_id.toString() + "_" + leftTable;
        rightTable = u_id.toString() + "_" + rightTable;
        List<String> leftlist = basicMapper.getFieldList(leftTable);
        List<String> rightlist = basicMapper.getFieldList(rightTable);

        HashMap<String,Object> resmap = new HashMap<String,Object>();

        resmap.put("leftField", leftlist);
        resmap.put("rightField", rightlist);

        return resmap;
    }
}
