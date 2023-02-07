package com.example.dataprocess.thread;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.example.dataprocess.mapper.BasicMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CreateTbThread implements Runnable {
    private List<HashMap<String, Object>> list;
    private CountDownLatch begin;
    private CountDownLatch end;
    private String tableName;
    private BasicMapper basicMapper;
    // 创建构造函数初始化 list 和其他参数
    public CreateTbThread(String tableName ,List<HashMap<String, Object>> list,BasicMapper basicMapper, CountDownLatch begin, CountDownLatch end) {
        this.list= list;
        this.begin = begin;
        this.end = end;
        this.tableName = tableName;
        this.basicMapper = basicMapper;
    }
    public CreateTbThread() {}

    @Override
    public void run() {
        try {
            basicMapper.insertByData(tableName,list);
            begin.await();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 线程执行完计数器减一
            end.countDown();
        }

    }
}

