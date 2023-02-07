package com.example.dataprocess.domain;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTableInfo {
    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
    private String db_type;
    private List<HashMap<String, Object>> check_tables;
    // private HashMap<String, Object> sync_fields;
    private String name;

    @Override
    public String toString() {
        return "CreateTableInfo [check_tables=" + check_tables + ", database=" + database + ", db_type=" + db_type
                + ", host=" + host + ", name=" + name + ", password=" + password + ", port=" + port + ", username="
                + username + "]";
    }

    

}
