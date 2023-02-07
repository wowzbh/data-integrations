package com.example.dataprocess.instance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private static String u_id; // 当前用户的u_id

    private static String MultiSql;  // 创建合表的sql语句




    private static User instance; // 唯一对象
    private static byte[] lock = new byte[0];
    
    // 单例模式，每次获取的都是同一对象
    public static synchronized User getInstance() {
        if(instance == null) {
            synchronized (lock) {
                if(instance == null)
                    instance = new User();
            }
        }

        return instance;
    }


    
}
