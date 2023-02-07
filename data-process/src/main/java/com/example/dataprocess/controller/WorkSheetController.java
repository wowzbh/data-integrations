package com.example.dataprocess.controller;

import com.example.dataprocess.service.WorkSheetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws")
public class WorkSheetController {
    @Autowired
    WorkSheetService workSheetService;

    @GetMapping("/tbinfo")
    public Object showTableName() {
        return workSheetService.getUserTableName();
    }

    @GetMapping("/fields")
    public Object showFields(@RequestParam("checkedTable") String checkedTable, @RequestParam("page") int page) {
        return workSheetService.getFieldsByCkTable(checkedTable, page);
        
    }


}
