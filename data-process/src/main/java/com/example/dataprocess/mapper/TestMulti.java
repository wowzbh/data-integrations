package com.example.dataprocess.mapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestMulti {

    @Autowired
    UserOracleMapper oracleMapper;

    public Object test(List<LinkedHashMap<String, Object>> reslist) {
        
        Iterator iter = reslist.iterator();
        String resSql = new String();
        Integer tmpId = 1;
        String sql1 = new String();
        String sql2 = new String();
        if(iter.hasNext()) {
            LinkedHashMap<String, Object> tmap = (LinkedHashMap<String, Object>) iter.next();
            sql1 = genString(10001, (String)tmap.get("leftName"));
            sql2 = genString(10001, (String)tmap.get("rightName"));
            String tmpName = tmpId.toString();
            resSql = "select * from (" + sql1 + ") \"" + tmpName + "_1\" "
            + (String) tmap.get("func") + 
            " (" + sql2 + ") \"" + tmpName  + 
            "_2\" on \"" + tmpName + "_1\".\"id_" + tmap.get("leftName") + "\""
            + "=\"" + tmpName + "_2\".\"id_" + tmap.get("rightName") + "\"";
            tmpId++;
        }

        // select * from sql1 tmpName1 left join sql2 tmpName2 on ...
        while(iter.hasNext()) {
            sql1 = resSql;
            LinkedHashMap<String, Object> tmap = (LinkedHashMap<String, Object>) iter.next();
            sql2 = genString(10001, (String)tmap.get("rightName"));
            String tmpName = tmpId.toString();

            resSql = "select * from (" + sql1 + ") \"" + tmpName + "_1\" "
                    + (String) tmap.get("func") + 
                    " (" + sql2 + ") \"" + tmpName + 
                    "_2\" on \"" + tmpName + "_1\".\"id_" + tmap.get("leftName") + "\""
                    + "=\"" + tmpName + "_2\".\"id_" + tmap.get("rightName") + "\"";
            
            tmpId++;
        } 

        return oracleMapper.get(resSql);

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
}
