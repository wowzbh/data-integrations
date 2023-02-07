package com.example.dataprocess.mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserOracleMapper {
    
    @Select("select table_name from user_tables")
    public List<HashMap<String, Object>> getOracleTableNames();

    // 查询指定的字段
    @Select("<script>                                  "
            + "select                                  "
            + "<foreach collection='fieldsNameList' item='item'  "
            + "   index='index' separator=','>         "
            + "   \"${item}\"                          "
            + "</foreach>                              "
            + "from \"${tableName}\"                   "
            + "</script>                               ")
    public List<HashMap<String, Object>> getCheckedFieldsByTableName(String tableName, List<String> fieldsNameList);


    // 查询表中的所有数据
    @Select("select * from \"${tableName}\"")
    public List<HashMap<String, Object>>getAllByTableName(String tableName); 

    // 获取指定用户数据表中的所有字段及其类型
   @Select("<script>                                  "
            + "select COLUMN_NAME,DATA_TYPE            "
            + "  from user_tab_columns where           "
            + " TABLE_NAME=#{tableName} and  (         "
            + "<foreach collection='list' item='item'  "
            + "   index='index' separator='or'>        "
            + "    COLUMN_NAME=#{item}                 "
            + "</foreach>       )                      "
            + "</script>                               ")
    public List<HashMap<String, Object>> getColumnxName(String tableName, List<String> list);

    // 查询指定表的所有字段名
    @Select("select column_name from user_tab_columns where table_name=#{tableName}")
    public List<String> getColumnName(String tableName);

    @Select("${sql}")
    public LinkedList<LinkedHashMap<String,Object>> get(String sql);

    // 创表表
    @Update("create table \"${tableName}\" as (${sql})")
    public int easyCreate(String tableName, String sql);
}
