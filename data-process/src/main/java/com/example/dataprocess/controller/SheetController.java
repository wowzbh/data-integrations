package com.example.dataprocess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dataprocess.service.ImportSheetService;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/ex")
public class SheetController {
    @Autowired
    ImportSheetService importService;

    @PostMapping("/upload")
    public Object uploadFile(MultipartFile file) throws IOException, InterruptedException {

        String fileName = file.getOriginalFilename();
        InputStream fileInputStream = file.getInputStream();
        importService.importExcel(fileName, fileInputStream);
        return HttpStatus.OK;
    }

    @GetMapping("/download")
    public void getFile(@RequestParam("checkedTable") String checkedTable, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        XSSFWorkbook workbook =  (XSSFWorkbook) importService.downloadFile(checkedTable);
        resp.setContentType("application/octet-stream;charset=utf-8");
        resp.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(checkedTable, "UTF-8"));
        ServletOutputStream out = resp.getOutputStream();
        workbook.write(out);
        out.close();
    }

}
