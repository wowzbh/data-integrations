package com.example.dataprocess.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.dataprocess.DynamicData.SwitchDataSource;
import com.example.dataprocess.mapper.BasicMapper;
import com.example.dataprocess.thread.CreateTbThread;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportSheetService {

    @Autowired
    BasicMapper basicMapper;

    public void importExcel(String fileName, InputStream fileInputStream) throws InterruptedException {
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            System.out.println("不是EXCEL类型");
            return;
        }
        // 创建工作簿
        Workbook workbook = null;
        if (fileName.endsWith(".xls"))
            try {
                workbook = new HSSFWorkbook(fileInputStream);
                fileName = fileName.replace(".xls", "");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        else
            try {
                workbook = new XSSFWorkbook(fileInputStream);
                fileName = fileName.replace(".xlsx", "");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        // 获取工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 获取字段名和属性
        List<HashMap<String, Object>> fieldlist = new LinkedList<>();
        // 获得表头
        Row rowHead = sheet.getRow(0);
        for (Cell cell : rowHead) {
            HashMap<String, Object> tmap = new HashMap<>();
            tmap.put("COLUMN_NAME", cell.getStringCellValue());
            tmap.put("DATA_TYPE", "VARCHAR2(255)");

            fieldlist.add(tmap);
        }
        // 获取字段名和字段值
        List<HashMap<String, Object>> fieldsValueList = new LinkedList<>();
        // 获取sheet的行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            // 过滤表头行
            if (i == 0) {
                continue;
            }
            // 获取当前行的数据
            Row row = sheet.getRow(i);
            int index = 0;
            HashMap<String, Object> tmap = new HashMap<>();
            for (Cell cell : row) {
                cell.setCellType(CellType.STRING);
                tmap.put(rowHead.getCell(index).getStringCellValue(), cell.getStringCellValue());
                index++;
            }
            fieldsValueList.add(tmap);
        }

        String tableName;
        Integer u_id = 10001;
        tableName = u_id.toString() + "_" + "EXCEL_" + fileName;
        // 连接默认数据源
        SwitchDataSource.setDefaultDataSource();
        // 根据字段名和属性创建表
        basicMapper.createTableByParams(tableName, fieldlist);
        // 多线程插入
        quickInsert(tableName, fieldsValueList, basicMapper);

        // 插入表信息到 USER_TABLE_INFO
        HashMap<String, Object> dMap = new HashMap<>();
        

        dMap.put("u_id", u_id); // 插入u_id数据
        dMap.put("tb_name", fileName); // 插入用户的选择的table名
        dMap.put("nv_tb_name", "EXCEL_" + fileName);
        basicMapper.insertSignal("USER_TABLE_INFO", dMap);
    }

    public Object downloadFile(String checkedTable) throws IOException {
        Integer u_id = 10001;
        String tableName = u_id.toString() + "_" + checkedTable;

        List<String> namelist = basicMapper.getFieldList(tableName);
        List<LinkedHashMap<String, Object>> mlist = basicMapper.getAllFields(tableName);
        //声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        //生成一个表格
        XSSFSheet sheet = workbook.createSheet();
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 18);
        // 创建标题行
        Row row = sheet.createRow(0);
        for (int j = 0; j< namelist.size(); j++){
            //创建列
            Cell cell = row.createCell(j);
            //设置单元类型为String
            cell.setCellType(CellType.STRING);
            cell.setCellValue((String)namelist.get(j));
        }
        System.out.println(namelist);
        //创建普通行
        Iterator iter = mlist.iterator();
        int index = 1;  // 0 用于创建表头
        while(iter.hasNext()) {
            row = sheet.createRow(index++);
            LinkedHashMap<String,Object> tmap = (LinkedHashMap<String,Object>) iter.next();
            Iterator miter = tmap.entrySet().iterator();
            int i = 0;
            while (miter.hasNext()) {
                //创建列
                Cell cell = row.createCell(i++);
                Map.Entry entry = (Map.Entry) miter.next();
                cell.setCellType(CellType.STRING);
                String value = (String) entry.getValue();
                cell.setCellValue(value);
            }
        }
         
         
        
        return workbook;

    }

    // 多线程插入数据
    private void quickInsert(String tableName, List<HashMap<String, Object>> list, BasicMapper basicMapper)
            throws InterruptedException {
        int count = 300; // 一个线程处理300条数据
        int listSize = list.size(); // 数据集合大小
        int runSize = (listSize / count) + 1; // 开启的线程数
        List<HashMap<String, Object>> newlist = null; // 存放每个线程的执行数据
        ExecutorService executor = Executors.newFixedThreadPool(runSize); // 创建一个线程池数量和开启线程的数量一样
        // 创建两个计数器
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(runSize);

        // 循环创建线程
        for (int i = 0; i < runSize; i++) {
            // 计算每个线程执行的数据
            if ((i + 1) == runSize) {
                int startIndex = (i * count);
                int endIndex = list.size();
                newlist = list.subList(startIndex, endIndex);
            } else {
                int startIndex = (i * count);
                int endIndex = (i + 1) * count;
                newlist = list.subList(startIndex, endIndex);
            }
            // 线程类
            CreateTbThread mythead = new CreateTbThread(tableName, newlist, basicMapper, begin, end);
            executor.execute(mythead);
        }
        begin.countDown();
        end.await();

        executor.shutdown();
    }

}
