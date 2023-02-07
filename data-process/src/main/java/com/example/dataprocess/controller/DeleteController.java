package com.example.dataprocess.controller;

import com.example.dataprocess.service.DeleteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delete")
public class DeleteController {
    @Autowired
    DeleteService deleteService;

    @GetMapping("/ds")
    public Object deleteDataSource(@RequestParam("dsName") String dsName) {
        System.out.println(dsName);
        deleteService.deleteDataSource(dsName);
        return HttpStatus.OK;
    }

}
