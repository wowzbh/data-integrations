package com.example.dataprocess.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.dataprocess.domain.DashChartInfo;
import com.example.dataprocess.domain.DashFieldInfo;
import com.example.dataprocess.domain.UploadInfo;
import com.example.dataprocess.service.DashBoardService;
import com.example.dataprocess.service.PyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dsh")
public class DashBoardController {

    @Autowired
    DashBoardService dashBoardService;

    @Autowired
    PyService pyService;

    @GetMapping("/table")
    public List<HashMap<String, Object>> getTableName() {
        return dashBoardService.getUserTableName();
    }

    @GetMapping("/field")
    public List<HashMap<String, Object>> getFieldsName(@RequestParam("table_name") String tableName) {
        return dashBoardService.getFieldsName(tableName);
    }

    @PostMapping("/field_data")
    public Object getFieldValue(@RequestBody DashFieldInfo dashFieldInfo) {

        System.out.println(dashFieldInfo);
        List<String> dimList = new LinkedList<>();
        List<String> numList = new LinkedList<>();

        Iterator dimIter = dashFieldInfo.getDimension().iterator();
        while (dimIter.hasNext()) {

            HashMap<String, Object> dmap = (HashMap<String, Object>) dimIter.next();
            dimList.add((String) dmap.get("COLUMN_NAME"));
        }

        Iterator numIter = dashFieldInfo.getNumber().iterator();
        while (numIter.hasNext()) {

            HashMap<String, Object> nmap = (HashMap<String, Object>) numIter.next();
            numList.add((String) nmap.get("COLUMN_NAME"));
        }

        return dashBoardService.getFieldsValue(dashFieldInfo.getTable_name(), dimList, numList);
    }

    @PostMapping("/create")
    public Object FinishedEdit(@RequestBody DashChartInfo dashChartInfo) {
        List<String> dimList = new LinkedList<>();
        List<String> numList = new LinkedList<>();

        Iterator dimIter = dashChartInfo.getDimension().iterator();
        while (dimIter.hasNext()) {

            HashMap<String, Object> dmap = (HashMap<String, Object>) dimIter.next();
            dimList.add((String) dmap.get("COLUMN_NAME"));
        }

        Iterator numIter = dashChartInfo.getNumber().iterator();
        while (numIter.hasNext()) {

            HashMap<String, Object> nmap = (HashMap<String, Object>) numIter.next();
            numList.add((String) nmap.get("COLUMN_NAME"));
        }

        if (!dashBoardService.insertChartOptions(dashChartInfo.getDashName(), dashChartInfo.getTable_name(), dimList,
                numList, dashChartInfo.getChart_type()))
            return new ResponseEntity<Object>(HttpStatus.METHOD_FAILURE);
        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }

    @GetMapping("/show")
    public Object showChart(@RequestParam("dashName") String dashName) {
        return dashBoardService.getChartInfo(dashName);
    }

    @GetMapping("/dshName")
    public Object showDash() {
        return dashBoardService.getDashName();
    }

    @GetMapping("/addDash")
    public Object addDash(@RequestParam("newDash") String newDash) {
        dashBoardService.createDash(newDash);
        return HttpStatus.OK;
    }
    @GetMapping("/delete")
    public Object deleteDash(@RequestParam("dashName") String dashName) {
        dashBoardService.deleteDash(dashName);
        return HttpStatus.OK;
    }

    @PostMapping("/upload")
    public Object getuploadFile(UploadInfo file) {

        // 得到上传时的文件名
        MultipartFile fileSource = file.getFile();
        String originName = file.getFile().getOriginalFilename();
        long size = file.getFile().getSize();
        System.out.println("originName: " + originName);
        System.out.println("fileName: " + file.getFile().getName());
        System.out.println("size: " + size);

        try {
            fileSource.transferTo(new File("/usr/local/project/python/yuce/" + fileSource.getOriginalFilename()));
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("dimension: " + file.getDimension());
        
        List<Object> date = dashBoardService.getyuceFields(file.getTableName(), file.getDimension());
        List<Object> value = dashBoardService.getyuceFields(file.getTableName(), file.getNumber());

        try {
            return pyService.yuce(fileSource.getOriginalFilename(), date, value);
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return HttpStatus.OK;
    }

    @GetMapping("/up_value")
    public Object getResult(@RequestParam("fileName") String fileName, 
                            @RequestParam("date") List<Object> date,
                            @RequestParam("value") List<Object> value) throws IOException, InterruptedException {
        return pyService.yuce(fileName, date, value);
    }
}