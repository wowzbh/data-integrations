package com.example.dataprocess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.dataprocess.DynamicData.DynamicDataSource;
import com.example.dataprocess.DynamicData.SwitchDataSource;
import com.example.dataprocess.domain.CreateTableInfo;
import com.example.dataprocess.mapper.BasicMapper;
import com.example.dataprocess.mapper.TestMapper;
import com.example.dataprocess.mapper.TestMulti;
import com.example.dataprocess.mapper.UserOracleMapper;
import com.example.dataprocess.service.ConnDatabaseService;
import com.example.dataprocess.service.CreateTableService;
import com.example.dataprocess.service.DashBoardService;
import com.example.dataprocess.service.ImportSheetService;
import com.example.dataprocess.service.PyService;
import com.example.dataprocess.service.WarnService;
import com.example.dataprocess.service.WeixinService;
import com.example.dataprocess.service.WorkSheetService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class DataProcessApplicationTests {

	@Autowired
	private BasicMapper mapper;

	@Autowired
	private UserOracleMapper userMapper;

	@Autowired
	private TestMapper testMapper;

	@Autowired
	ConnDatabaseService connDatabaseService;

	@Autowired
	CreateTableService createTableService;

	@Autowired
	WorkSheetService workSheetService;

	@Autowired
	DashBoardService dashBoardService;

	@Autowired
	TestMulti testMultiService;

	@Autowired
	WarnService warnService;

	@Autowired
	PyService pyService;

	@Autowired
	ImportSheetService importService;

	@Autowired
	WeixinService wxService;

	@Test
	void contextLoads() throws InterruptedException, IOException {
		// 测试创建表
		// List<HashMap<String, Object>> createlist = new LinkedList<>();
		// HashMap<String, Object> map1 = new HashMap<>();
		// HashMap<String, Object> map2 = new HashMap<>();

		// map1.put("COLUMN_NAME","id");
		// map1.put("DATA_TYPE", "number");
		// map2.put("COLUMN_NAME", "name");
		// map2.put("DATA_TYPE", "varchar2(255)");

		// createlist.add(map1);
		// createlist.add(map2);
		// mapper.createTableByParams("sss", createlist);

		// 测试插入数据
		// List<HashMap<String, Object>> insertlist = new LinkedList<>();
		// HashMap<String, Object> map3 = new HashMap<>();
		// HashMap<String, Object> map4 = new HashMap<>();

		// map3.put("id",1);
		// map3.put("name", "aaa");
		// map4.put("id", 2);
		// map4.put("name", "bbb");

		// insertlist.add(map3);
		// insertlist.add(map4);

		// mapper.insertByData("sss", insertlist);

		// 测试获取表名
		// System.out.println(mapper.getTableNameById(10001));

		// 测试 ConnDatabaseService
		// DruidDataSource druidDataSource = new DruidDataSource();
		// druidDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		// druidDataSource.setUrl("jdbc:oracle:thin:@47.102.146.116:1521:orcl");
		// druidDataSource.setUsername("TEST1");
		// druidDataSource.setPassword("123456");

		// connDatabaseService.setDataSource(druidDataSource, "test");

		// System.out.println(mapper.getFieldByTableName("USER0"));

		// connDatabaseService.setDefaultDataSource();

		// System.out.println(mapper.getFieldByTableName("USER_INFO"));
		// 测试 UserMapper
		// List<String> list = new LinkedList<>();
		// list.add("u_id");
		// list.add("username");

		// System.out.println(testMapper.getCheckedFieldsByTableName("USER_INFO",
		// list));

		// 测试 creatTable
		// DruidDataSource druidDataSource = new DruidDataSource();
		// druidDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		// druidDataSource.setUrl("jdbc:oracle:thin:@47.102.146.116:1521:orcl");
		// druidDataSource.setUsername("TEST1");
		// druidDataSource.setPassword("123456");

		// List<String> checkedList = new LinkedList<>();
		// checkedList.add("id");
		// checkedList.add("name");
		// createTableService.createTable("INFO", druidDataSource, "INFO", checkedList);

		// 测试createController
		// CreateTableInfo createTableInfo = new CreateTableInfo();
		// List<String> l = new LinkedList<>();
		// l.add("USER0");
		// l.add("INFO");
		// createTableInfo.setCheck_tables(l);
		// createTableInfo.setDatabase("orcl");
		// createTableInfo.setDb_type("1");
		// createTableInfo.setHost("47.102.146.116");
		// createTableInfo.setName("name");
		// createTableInfo.setUsername("TEST");
		// createTableInfo.setPassword("123456");
		// createTableInfo.setPort("1521");
		// List<String> l0 = new LinkedList<>();
		// List<String> l1 = new LinkedList<>();
		// l0.add("id");
		// l0.add("age");
		// l1.add("id");
		// l1.add("name");
		// HashMap<String, Object> map = new HashMap<>();
		// map.put("USER0", l0);
		// map.put("INFO", l1);
		// createTableInfo.setSync_fields(map);
		// String driverClass = new String();
		// String url = new String();

		// if(createTableInfo.getDb_type().equals("1")) {
		// driverClass = "oracle.jdbc.driver.OracleDriver";
		// url = "jdbc:oracle:thin:@" + createTableInfo.getHost() + ":" +
		// createTableInfo.getPort() + ":" + "orcl";
		// }
		// DruidDataSource userDataSource = new DruidDataSource();

		// userDataSource.setDriverClassName(driverClass);
		// userDataSource.setUrl(url);
		// userDataSource.setUsername(createTableInfo.getUsername());
		// userDataSource.setPassword(createTableInfo.getPassword());

		// Iterator iter_table = createTableInfo.getCheck_tables().iterator();
		// while(iter_table.hasNext()) {

		// String tableName = (String)iter_table.next();
		// List<String> ck_fields = (List<String>)
		// createTableInfo.getSync_fields().get(tableName);
		// createTableService.createTable(tableName, userDataSource,
		// createTableInfo.getName(), createTableInfo.getDb_type(),ck_fields);
		// }
		// createTableService.insertDatabase(createTableInfo.getDb_type(),
		// createTableInfo.getName(), userDataSource);

		// System.out.println(createTableService.isCreate("123"));

		// 测试 WorkSheet
		// System.out.println(workSheetService.getUserTableName());
		// System.out.println(workSheetService.getFieldsByCkTable("name_USER0"));
		// System.out.println(testMapper.getColumnName("USER_TABLE_INFO"));
		// 测试DashBoard
		// System.out.println(dashBoardService.getUserTableName());
		// System.out.println(dashBoardService.getFieldsName("name_INFO"));
		// System.out.println(dashBoardService.getFieldsValue("name_INFO", "name"));

		// CreateTableInfo createTableInfo = new CreateTableInfo();
		// List<HashMap<String, Object>> l = new LinkedList<HashMap<String, Object>>();
		// HashMap<String, Object> map1 = new HashMap<>();
		// HashMap<String, Object> map2 = new HashMap<>();
		// map1.put("TABLE_NAME", "USER");
		// map2.put("TABLE_NAME", "USER0");
		// l.add(map1);
		// l.add(map2);

		// createTableInfo.setCheck_tables(l);
		// createTableInfo.setDatabase("orcl");
		// createTableInfo.setDb_type("1");
		// createTableInfo.setHost("47.102.146.116");
		// createTableInfo.setName("11");
		// createTableInfo.setUsername("TEST1");
		// createTableInfo.setPassword("123456");
		// createTableInfo.setPort("1521");

		// if(!createTableService.isCreate(createTableInfo.getName()))
		// return ;

		// String driverClass = new String();
		// String url = new String();

		// if(createTableInfo.getDb_type().equals("1")) {
		// driverClass = "oracle.jdbc.driver.OracleDriver";
		// url = "jdbc:oracle:thin:@" + createTableInfo.getHost() + ":" +
		// createTableInfo.getPort() + ":" + "orcl";
		// }
		// DruidDataSource userDataSource = new DruidDataSource();

		// userDataSource.setDriverClassName(driverClass);
		// userDataSource.setUrl(url);
		// userDataSource.setUsername(createTableInfo.getUsername());
		// userDataSource.setPassword(createTableInfo.getPassword());

		// Iterator iter_table = createTableInfo.getCheck_tables().iterator();
		// while(iter_table.hasNext()) {

		// HashMap<String, String> map = (HashMap<String, String>) iter_table.next();
		// String tableName = map.get("TABLE_NAME");
		// // List<String> ck_fields = (List<String>)
		// createTableInfo.getSync_fields().get(tableName);
		// // List<String> ck_fields = createTableService.getFieldsName(tableName);
		// // System.out.println(ck_fields);
		// createTableService.createTable(tableName, userDataSource,
		// createTableInfo.getName(), createTableInfo.getDb_type());
		// }

		// createTableService.insertDatabase(createTableInfo.getDb_type(),
		// createTableInfo.getName(), userDataSource);

		// return ;
		// List<String> ck_fields_list =
		// createTableService.getFieldsName("USER_TABLE_INFO");
		// System.out.println(userMapper.getCheckedFieldsByTableName("USER_TABLE_INFO",
		// ck_fields_list));

		// DruidDataSource userDataSource = new DruidDataSource();
		// userDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		// userDataSource.setUrl("jdbc:oracle:thin:@47.102.146.116:1521:orcl");
		// userDataSource.setUsername("TEST");
		// userDataSource.setPassword("123456");
		// String tb_name = "COMPANY_INFO";
		// String nv_db_name = "xxxxxx";
		// SwitchDataSource.setDataSource(userDataSource, nv_db_name);
		// List<String> ck_fields_list = createTableService.getFieldsName(tb_name);
		// // 获取字段名和属性
		// List<HashMap<String, Object>> fields = userMapper.getColumnxName(tb_name,
		// ck_fields_list);
		// // 获取字段名和字段值
		// List<HashMap<String, Object>> fieldsValueList =
		// userMapper.getCheckedFieldsByTableName(tb_name, ck_fields_list);
		// // List<HashMap<String, Object>> fieldsValueList = null;
		// String tableName;
		// Integer u_id = 10001;
		// tableName = u_id.toString()+ "_" + nv_db_name + "_" + tb_name;

		// // 连接默认数据源
		// SwitchDataSource.setDefaultDataSource();
		// // 根据字段名和属性创建表
		// Iterator iter = fields.iterator();
		// while(iter.hasNext()) {
		// HashMap<String, Object> map = (HashMap<String, Object>) iter.next();
		// if( map.get("DATA_TYPE").equals("VARCHAR2"))
		// map.put("DATA_TYPE","VARCHAR2(255)");
		// }
		// mapper.createTableByParams(tableName, fields);

		// 根据字段名和字段值插入
		// thread.exec(tableName, fieldsValueList, mapper);

		// 测试DataSource
		// System.out.println(mapper.getDbConnNames(10001));
		// System.out.println(mapper.getFieldByTableName("10001_11_USER0"));

		// String tableName = "11_USER";

		// List<String> dimList = new LinkedList<>();
		// List<String> numList = new LinkedList<>();

		// dimList.add("id");
		// dimList.add("age");
		// numList.add("name");
		// numList.add("id");

		// Integer u_id = 10001;
		// tableName = u_id.toString()+ "_" + tableName;
		// HashMap<String,HashMap<String, Object>> resMap = new HashMap<>();
		// HashMap<String, Object> dimMap = new HashMap<>();
		// HashMap<String, Object> numMap = new HashMap<>();

		// List<Object> fieldValue = null; // 存放字段的数据

		// Iterator dimIter = dimList.iterator();
		// while(dimIter.hasNext()) {
		// String fieldName = (String)dimIter.next();
		// fieldValue = mapper.getFieldsValue(tableName, fieldName);
		// dimMap.put(fieldName,fieldValue);
		// }
		// Iterator numIter = numList.iterator();
		// while(numIter.hasNext()) {
		// String fieldName = (String)numIter.next();
		// fieldValue = mapper.getFieldsValue(tableName, fieldName);
		// numMap.put(fieldName,fieldValue);
		// }

		// resMap.put("dimension", dimMap);
		// resMap.put("number", numMap);
		// System.out.println(resMap);
		// System.out.println(testMapper.testInner());

		// String tableName = "11_USER";
		// List<String> dimList = new LinkedList<>();
		// List<String> numList = new LinkedList<>();
		// dimList.add("id");
		// dimList.add("name");
		// numList.add("age");
		// numList.add("id");
		// String chartType = "1";
		// dashBoardService.insertChartOptions(tableName, dimList, numList, chartType);
		// System.out.println(mapper.getChartsInfo(10001, "test_name"));
		// String str = "id,name,age";
		// List<String> l= Arrays.asList(str.split(","));
		// System.out.println(l);
		// System.out.println(dashBoardService.getChartInfo());
		// System.out.println(testMapper.testFilter("10001_11_USER", "id", "<=", 1));

		// System.out.println(testMultiService.test(null));
		// System.out.println(System.getProperty("user.dir"));
		// List<LinkedHashMap<String, Object>> reslist = new LinkedList<>();
		// mapper.setWarnInfo("USER_TABLE_INFO", "u_id", "=", 11);
		// System.out.println(mapper.getFieldList("10001_111_INFO"));
		// System.out.println(mapper.getWarn("10001_111_INFO", "id", "<=", 225));
		// warnService.add("111_INFO", "222", "id", "等于", 226);
		// warnService.add("111_INFO", "222", "id", "大于", 226);
		// System.out.println(warnService.showWarnInfo());
		// 连接mysql

		// String driverClass = "com.mysql.cj.jdbc.Driver";
		// String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
		// DruidDataSource userDataSource = new DruidDataSource();

		// userDataSource.setDriverClassName(driverClass);
		// userDataSource.setUrl(url);
		// userDataSource.setUsername("root");
		// userDataSource.setPassword("001109");
		// SwitchDataSource.setDataSource(userDataSource, userDataSource.getUrl());
		// System.out.println(testMapper.testMysql());

		// warnService.getRownum("");
		// String content = "[1,2,3]";
		// JSONArray jsonObject = JSONArray.parseArray(content);
		// List<String> list = JSONObject.parseArray(content, String.class);
		// System.out.println(list);
		// String str = "[3. 3. 3.]";
		// String str2 = str.substring(1,str.length()-1);
		// System.out.println("str: " + str2);
		// List<String> s = Arrays.asList(str2.split(" "));
		// System.out.println(s);

		// Excel 文件导入
		// try {
		// 	InputStream in = new FileInputStream("D:/base.xlsx");
		// 	importService.importExcel("base.xlsx", in);
		// } catch (FileNotFoundException e) {
		// 	// TODO Auto-generated catch block
		// 	e.printStackTrace();
		// }

		// Excel 下载
		// importService.downloadFile("EXCEL_base");
		// System.out.println(wxService.getFieldValue());
	}

}
