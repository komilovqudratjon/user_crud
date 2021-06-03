package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUploadFile {
    private Long fileId;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
