package com.example.dataprocess.mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BasicMapper {

    // 创建用户的数据表
    // fields： key[COLUMN_NAME, DATA_TYPE], 
    // DATA_TYPE: sql原生的字段类型
    @Update("<script>                                     "
            + "create table                               "
            + "\"MAINDATABASEOFPROCESS\".\"${tableName}\" "
            + "(                                          "
            + "<foreach collection='fields' item='item'   "
            + " index='index' separator=','>              "
            + " \"${item.COLUMN_NAME}\"                   "
            + "   ${item.DATA_TYPE}                       "
            + "</foreach>  )                              "
            + "</script>                                  ")
    public int createTableByParams(String tableName, List<HashMap<String,Object>> fields);
    @Update("<script>                                     "
            + "create table                               "
            + "\"MAINDATABASEOFPROCESS\".\"${tableName}\" "
            + "(                                          "
            + "<foreach collection='fields' item='item'   "
            + " index='index' separator=','>              "
            + " \"${item.column_name}\"                   "
            + "   ${item.data_type}                       "
            + "</foreach>  )                              "
            + "</script>                                  ")
    public int createMySqlTableByParams(String tableName, List<HashMap<String,Object>> fields);


    @Select("select count(*) from \"${tableName}\"")
    public int getCount(String tableName);

    // 查找用户创建的所有数据表的名字
    @Select("select \"nv_tb_name\" from USER_TABLE_INFO where \"u_id\"=#{id}")
    public List<HashMap<String, Object>> getTableNameById(Integer id);
    
    // 查找用户创建的所有数据库链接
    @Select("SELECT \"nv_db_name\", \"db_type\" FROM \"USER_DATABASE_INFO\" where \"u_id\"=#{u_id}")
    public List<HashMap<String, Object>> getDbConnNames(Integer u_id);

    // 查找指定表的所有字段名
    @Select("select column_name from user_tab_columns where table_name=#{tableName}")
    public List<HashMap<String, Object>> getFieldByTableName(String tableName);

    // 字段名 List
    @Select("select column_name from user_tab_columns where table_name=#{tableName}")
    public List<String> getFieldList(String tableName);

    // 查找USER_DATABASE_INFO中的nv_db_name
    @Select("select \"nv_db_name\" from USER_DATABASE_INFO where \"nv_db_name\"=#{dbName}")
    public HashMap<String, String> getDbName(String dbName);

    // 查找指定表的所有信息
    @Select("select * from \"${tableName}\"")
    public List<LinkedHashMap<String, Object>> getAllFields(String tableName);

        
    // 分页查询
    @Select("SELECT M.* FROM (SELECT ROWNUM AS RN, T.* FROM \"${tableName}\" T WHERE ROWNUM <= #{end}) M WHERE RN >= #{start}")
    public List<HashMap<String,Object>> getFieldsByPage(List<String> fieldsNameList,String tableName, int start, int end);

    // 查找字段的所有信息
    @Select("select \"${fieldsName}\" from \"${tableName}\"")
    public List<Object> getFieldsValue( String tableName, String fieldsName);

    // 查找指定用户的所有 chart 图表信息
    @Select("select * from \"USER_DASHBOARD_INFO\" where \"u_id\"=#{u_id} and \"dash_name\"=#{dashName}")
    public List<LinkedHashMap<String, Object>> getChartsInfo(Integer u_id, String dashName);

    // 根据 warn条件设置预警数据
    @Update("update \"${tableName}\" set \"warn_flag\"=1 where \"${fieldName}\"${warnType}#{warnValue}")
    public int setWarnInfo(String tableName, String fieldName, String warnType, Object warnValue);

    // 根据warn条件查找预警数据
    @Select("select * from \"${tableName}\" where \"${fieldName}\"${warnType}#{warnValue}")
    public LinkedList<LinkedHashMap<String,Object>> getWarn(String tableName, String fieldName, String warnType, Object warnValue);
   
    // 根据warn条件查找预警字段
    @Select("select \"${key}\" from \"${tableName}\" where \"${fieldName}\"${warnType}#{warnValue}")
    public LinkedList<Object> getFieldWarn(String key,String tableName, String fieldName, String warnType, Object warnValue);

    // 初始化预警值
    @Update("update \"${tableName}\" set \"warn_flag\"=0")
    public int initWarnInfo(String tableName);

    // 查找指定用户的 warn信息
    @Select("select * from \"USER_WARN_INFO\" where \"u_id\"=#{u_id}")
    public LinkedList<LinkedHashMap<String,Object>> getWarnById(Integer u_id);

    @Select("select * from \"USER_WARN_INFO\" where \"u_id\"=#{u_id} and \"tb_name\"=#{tableName}")
    public LinkedList<LinkedHashMap<String,Object>> getWarnByTb(Integer u_id,String tableName);

    @Select("select \"field_name\" from \"USER_WARN_INFO\" where \"u_id\"=#{u_id} and \"tb_name\"=#{tableName}")
    public LinkedList<String> getWarnFieldByTb(Integer u_id,String tableName);
    // 向指定表插入数据
    // list: key[field1, ... ,field2]
    @Insert("<script>                                  "
            + "insert all                              "
            + "<foreach collection='list' item='list_item'  "
            + " index='index' separator=' '>           "
            + " into \"${tableName}\"(                 "
            + "  <foreach collection='list_item' item='item'"
            + "   index='key' separator=','>           "
            + "   \"${key}\"                           "
            + "  </foreach>                            "
            + "    )                                   "
            + " values (                               "
            + "  <foreach collection='list_item' item='item'"
            + "   index='key' separator=','>           "
            + "   #{list_item.${key}}                  "
            + "  </foreach>                            "
            + "  )                                     "
            + "</foreach>                              "
            + "select 1 from dual                      "
            + "</script>                               ")
    public int insertByData(String tableName, List<HashMap<String,Object>> list);

    @Insert("<script>                                  "
            + "insert  into \"${tableName}\"(          "
            + "  <foreach collection='dMap' item='item'"
            + "   index='key' separator=','>           "
            + "   \"${key}\"                           "
            + "  </foreach>                            "
            + "    )                                   "
            + " values (                               "
            + "  <foreach collection='dMap' item='item'"
            + "   index='key' separator=','>           "
            + "   #{dMap.${key}}                  "
            + "  </foreach>                            "
            + "  )                                     "
            + "</script>                               ")
    public int insertSignal(String tableName, HashMap<String, Object> dMap);

    
    // 删除表
    @Update("drop table \"${tableName}\"")
    public int deleteTable(String tableName);

    // 删除数据
    @Delete("delete from \"${tableName}\" where \"${fieldName}\"=#{fieldValue}")
    public int deleteValue(String tableName, String fieldName, Object fieldValue);

    // 根据 数据源连接名查找所有表名
    @Select("select \"nv_tb_name\" from USER_TABLE_INFO where \"conn_db_name\"=#{dbName}")
    public List<String> getdeleteName(String dbName);

    // 查询仪表盘名称
    @Select("select \"dash_name\" from USER_DASH_NAME where \"u_id\"=#{u_id}")
    public List<String> getDashName(Integer u_id);
    
}
