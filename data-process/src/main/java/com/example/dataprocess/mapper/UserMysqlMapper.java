package com.example.dataprocess.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMysqlMapper {

    @Select("select table_name as TABLE_NAME from information_schema.tables where table_schema=#{dbName}")
    public List<HashMap<String, Object>> getTableNames(String dbName);

    // 查询指定表的所有字段名
    @Select("select column_name from information_schema.columns where table_schema=#{dbName} and table_name=#{tableName}")
    public List<String> getColumnName(String dbName, String tableName);

     // 获取指定用户数据表中的所有字段及其类型
    @Select("<script>                                  "
        + "select column_name,data_type            "
        + "from information_schema.columns         "
        + "where table_schema=#{dbName} and        "
        + " table_name=#{tableName} and  (         "
        + "<foreach collection='list' item='item'  "
        + "   index='index' separator='or'>        "
        + "    COLUMN_NAME=#{item}                 "
        + "</foreach>       )                      "
        + "</script>                               ")
    public List<HashMap<String, Object>> getColumnxName(String dbName, String tableName, List<String> list);

     // 查询指定的字段
    @Select("<script>                                  "
     + "select                                  "
     + "<foreach collection='fieldsNameList' item='item'  "
     + "   index='index' separator=','>         "
     + "   ${item}                              "
     + "</foreach>                              "
     + "from ${tableName}                       "
     + "</script>                               ")
    public List<HashMap<String, Object>> getCheckedFieldsByTableName(String tableName, List<String> fieldsNameList);
}
