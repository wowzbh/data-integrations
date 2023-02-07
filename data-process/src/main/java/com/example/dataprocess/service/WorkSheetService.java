package com.example.dataprocess.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.example.dataprocess.mapper.BasicMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkSheetService {

    @Autowired
    BasicMapper basicMapper;

    public List<HashMap<String, Object>> getUserTableName() {
        return basicMapper.getTableNameById(10001);
    }

    public Object getFieldsByCkTable(String checkedTable, int page) {

        Integer u_id = 10001;
        String tableName = u_id.toString() + "_" + checkedTable;
        // 一页 100 分页
        int startrow = 100 * page;
        int endrow = startrow + 100;
        int maxrow = basicMapper.getCount(tableName);

        if (startrow > maxrow)
            return null;
        else if (endrow < maxrow) {
            List<String> namelist = basicMapper.getFieldList(tableName);
            Iterator iter = namelist.iterator();
            while(iter.hasNext()) {
                String field = (String) iter.next();
                if(field.equals("RN")){
                    iter.remove();
                    break;
                }
            }
            // List<HashMap<String, Object>> map = basicMapper.getAllFields(tableName);
            List<HashMap<String, Object>> map = basicMapper.getFieldsByPage(namelist, tableName, startrow, endrow);

            HashMap<String, Object> resmap = new LinkedHashMap<>();
            List<Integer> rowlist = getRownum(checkedTable);
            resmap.put("name", namelist);
            resmap.put("value", map);
            resmap.put("row", rowlist);
            return resmap;
        } else if(endrow > maxrow) {
            endrow = maxrow;
            List<String> namelist = basicMapper.getFieldList(tableName);
            Iterator iter = namelist.iterator();
            while(iter.hasNext()) {
                String field = (String) iter.next();
                if(field.equals("RN")){
                    iter.remove();
                    break;
                }
            }
            // List<HashMap<String, Object>> map = basicMapper.getAllFields(tableName);
            List<HashMap<String, Object>> map = basicMapper.getFieldsByPage(namelist, tableName, startrow, endrow);

            HashMap<String, Object> resmap = new LinkedHashMap<>();
            List<Integer> rowlist = getRownum(checkedTable);
            resmap.put("name", namelist);
            resmap.put("value", map);
            resmap.put("row", rowlist);
            return resmap;
        }
        return null;
        
    }

    private List<Integer> getRownum(String tableName) {
        Integer u_id = 10001;
        String tmpKey = new String();

        LinkedList<LinkedHashMap<String, Object>> rlist = basicMapper.getWarnByTb(u_id, tableName);
        if(rlist.size() == 0)
            return new LinkedList();
        LinkedList<Object> reslist = new LinkedList<>();
        Iterator iter = rlist.iterator();
        int f = 0;
        while (iter.hasNext()) {
            LinkedHashMap<String, Object> tmap = (LinkedHashMap<String, Object>) iter.next();
            String tbName = u_id.toString() + "_" + tableName;
            String fieldName = (String) tmap.get("field_name");
            if (f++ == 0)
                tmpKey = fieldName;
            String warnType = (String) tmap.get("warn_type");
            Integer warnValue = Integer.valueOf(tmap.get("warn_value").toString());
            reslist.addAll(basicMapper.getFieldWarn(tmpKey, tbName, fieldName, warnType, warnValue));
        }

        int rowflag = 0;
        List<Integer> res = new LinkedList<>();
        List<LinkedHashMap<String, Object>> vlist = basicMapper.getAllFields(u_id.toString() + "_" + tableName);
        iter = vlist.iterator();

        while (iter.hasNext()) {
            HashMap<String, Object> tMap = (HashMap<String, Object>) iter.next();
            Object val = tMap.get(tmpKey);
            for (Object value : reslist) {
                if (value.equals(val)) {
                    Integer row = rowflag;
                    res.add(row);
                    break;
                }
            }
            rowflag++;
        }
        return res;
    }

}
