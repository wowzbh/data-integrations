package com.example.dataprocess.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import org.springframework.stereotype.Service;

@Service
public class PyService {
    public String update(String inStr, String runfile, String writefile, String readfile)
            throws IOException, InterruptedException {
        String path = System.getProperty("user.dir");
        String[] command = new String[3];
        command[0] = "#!/bin/sh";
        command[1] = "cd /usr/local/project/python/";
        command[2] = "python3 " + runfile;
        System.out.println("path: " + path);

        // 写入point.json 文件 point.json
        FileWriter fw;
        try {
            fw = new FileWriter("/usr/local/project/python/" + writefile);
            PrintWriter out = new PrintWriter(fw);
            out.write(inStr);
            out.println();
            fw.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(path + "/run.sh");
        fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(command[0]);
            bw.newLine();
            bw.write(command[1]);
            bw.flush();
            bw.newLine();
            bw.write(command[2]);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 运行 命令行脚本
        String batPath = "sh " + path + "/run.sh";
        System.out.println("batPath: " + batPath);
        Process ps = Runtime.getRuntime().exec(batPath);
        ps.waitFor();

        // 读取 data.json文件
        FileReader reader = new FileReader("/usr/local/project/python/" + readfile);
        BufferedReader br = new BufferedReader(reader);
        String resLine = new String();
        String line;
        while ((line = br.readLine()) != null) {
            resLine += line;
            System.out.println(line);
        }

        return resLine;

    }

    public HashMap<String, Object> yuce(String runfile, List<Object> dlist,List<Object> rlist) throws IOException, InterruptedException {
        
        String path = System.getProperty("user.dir");
        String[] command = new String[3];
        String date = dlist.toString().replace(" ", "");
        String number = rlist.toString().replace(" ", "");
        command[0] = "#!/bin/sh";
        command[1] = "cd /usr/local/project/python/yuce";
        command[2] = "python3 " + runfile + " " + date + " " + number;
        File file = new File(path + "/run.sh");
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(command[0]);
            bw.newLine();
            bw.write(command[1]);
            bw.flush();
            bw.newLine();
            bw.write(command[2]);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 运行 命令行脚本
        String batPath = "sh " + path + "/run.sh";
        System.out.println("batPath: " + batPath);
        Process ps = Runtime.getRuntime().exec(batPath);

        InputStream inputstream = ps.getInputStream();
        BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputstream));
        String str1 = bufferreader.readLine();
        String str2 = bufferreader.readLine();
        String res1 = str1.substring(1,str1.length()-1);
        String res2 = str2.substring(1,str2.length()-1);
        System.out.println("dlist: " + dlist.toString());
        System.out.println("rlist: " + rlist.toString());
        List<String> resl1 = Arrays.asList(res1.split(" "));
        List<String> resl2 = Arrays.asList(res2.split(" "));
        dlist.addAll(resl1);
        rlist.addAll(resl2);
        ps.waitFor();
        HashMap<String,Object> rmap = new HashMap<>();
        rmap.put("dimension", dlist);
        rmap.put("number", rlist);
        return rmap;
    }

    public void save() throws IOException {

        // 初始化 data.json 文件
        FileWriter fw;
        try {
            fw = new FileWriter("/usr/local/project/python/data.json");
            PrintWriter out = new PrintWriter(fw);
            out.write("{}");
            out.println();
            fw.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
