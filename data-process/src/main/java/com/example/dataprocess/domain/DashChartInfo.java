package com.example.dataprocess.domain;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashChartInfo {
    String dashName;
    String table_name;
    List<HashMap<String, Object>> dimension;
    List<HashMap<String, Object>> number;
    String chart_type;
}
