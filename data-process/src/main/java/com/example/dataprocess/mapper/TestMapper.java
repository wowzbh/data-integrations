package com.example.dataprocess.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TestMapper {
    @Insert("<script>                                  "
            + "insert all                              "
            + "<foreach collection='list' item='item'  "
            + " index='index' separator=' '>           "
            + " into USER1(\"id\", \"name\",\"age\")   "
            + " values (#{item.id},#{item.name},#{item.age}) "
            + "</foreach>                              "
            + "select 1 from dual                      "
            + "</script>                               ")
    int insertUserList(List<HashMap<String,Object>> list);

    @Select("select column_name from user_tab_columns where table_name=#{tableName}")
    public List<String> getColumnName(String tableName);

    @Update("<script>                                  "
            + "create table \"MAINDATABASEOFPROCESS\".\"${tableName}\" "
            + "( \"id\" number,                        "
            + " \"name\" VARCHAR2(255)                )"
            + "</script>                               ")
    public int createTable_(String tableName);

    @Select("select * from \"${tableName}\"")
    public List<HashMap<String, Object>>getAllByTableName(String tableName);  

    @Select("<script>                                  "
            + "select COLUMN_NAME,DATA_TYPE            "
            + "  from user_tab_columns where           "
            + " TABLE_NAME=#{tableName} and            "
            + "<foreach collection='list' item='item'  "
            + "   index='index' separator='or'>        "
            + "    COLUMN_NAME=#{item}                 "
            + "</foreach>                              "
            + "</script>                               ")
    public List<HashMap<String, Object>> getColumnxName(String tableName, List<String> list);

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

     
    @Select("select * from (select * from \"10001_11_USER\" inner JOIN \"10001_name_INFO\" on \"10001_11_USER\".\"id\"=\"10001_name_INFO\".\"id\")")
    public List<HashMap<String, Object>> testInner();

    @Select("select * from \"${tableName}\" where \"${field}\"${type}#{value}")
    public List<HashMap<String,Object>> testFilter(String tableName, String field, String type, Object value);

    @Select("select table_name from information_schema.tables where table_schema='test'")
    public List<HashMap<String,Object>> testMysql();

    @Select("select * from staff")
    public List<HashMap<String,Object>> getWxValue();
}
