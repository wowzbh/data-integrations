package com.example.dataprocess.domain;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarnInfo {
    String tableName;
    String warnName;
    // String fieldName;
    // String warnType;
    // Integer warnValue;
    LinkedHashMap<String,Object> warnMap;

    @Override
    public String toString() {
        return "WarnInfo [tableName=" + tableName + ", warnName=" + warnName + ", warnMap=" + warnMap + "]";
    }
}
