package com.example.dataprocess.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.example.dataprocess.domain.MultiTableInfo;
import com.example.dataprocess.service.MultiTableService;
import com.example.dataprocess.service.PyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mt")
public class MultiTableController {
    @Autowired
    PyService pyService;

    @Autowired
    MultiTableService multiService;

    @PostMapping("/ins")
    public Object insert(@RequestBody String inStr) {
        
        String str = new String();
        try {
            str = pyService.update(inStr, "insert.py", "point.json","data.json");
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }


    @PostMapping("/delete")
    public Object delete(@RequestBody String inStr) {
        
        String str = new String();
        try {
            str = pyService.update(inStr, "delete.py","point.json","data.json");
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
    @PostMapping("/show_")
    public Object show_(@RequestBody String inStr) {
        
        String str = new String();
        try {
            str = pyService.update(inStr, "show_.py", "show.json","show.json");
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
    @PostMapping("/revise_")
    public Object revise_(@RequestBody String inStr) {
        
        String str = new String();
        try {
            str = pyService.update(inStr, "revise_.py", "revise.json","data.json");
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    @GetMapping("/field")           
    public HashMap<String, Object> getFieldsName(@RequestParam("leftTable") String leftTable,@RequestParam("rightTable") String rightTable) {
        return multiService.getFieldsName(leftTable, rightTable);
    }

    @PostMapping("/save")
    public Object testM(@RequestBody MultiTableInfo multiInfo) {
        System.out.println(multiInfo);
        LinkedHashMap<String, Object> test = multiInfo.getMap();
        
        System.out.println(test);
        List<LinkedHashMap<String, Object>> reslist = new LinkedList<>();
        LinkedHashMap<String, Object> lmap = new LinkedHashMap<>();
        String lstr = new String();
        multiService.multiTable(test, reslist, lstr);
        
        multiService.create(reslist, multiInfo.getName());

        try {
            pyService.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(multiService.test(reslist));

        // System.out.println(reslist);
        return HttpStatus.OK;
    }
}
