package com.example.dataprocess.domain;

import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiTableInfo {
    LinkedHashMap<String,Object> map;
    String name;

    @Override
    public String toString() {
        return "MultiTableInfo [map=" + map + ", name=" + name + "]";
    }
    
}
