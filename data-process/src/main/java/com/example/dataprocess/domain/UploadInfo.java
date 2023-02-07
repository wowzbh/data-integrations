package com.example.dataprocess.domain;

import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadInfo {
    MultipartFile file;
    String tableName;
    String dimension;
    String number;

    @Override
    public String toString() {
        return "UploadInfo [dimension=" + dimension + ", file=" + file + ", number=" + number + ", tableName="
                + tableName + "]";
    }
    
}
