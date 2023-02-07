package com.example.dataprocess.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataBaseInfo {

    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
    private String db_type;

}
