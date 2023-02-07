package com.example.dataprocess.domain;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashFieldInfo {
    String table_name;
    List<HashMap<String, Object>> dimension;
    List<HashMap<String, Object>> number;

    @Override
    public String toString() {
        return "DashFieldInfo [demension=" + dimension + ", number=" + number + ", table_name=" + table_name + "]";
    }

    
}
