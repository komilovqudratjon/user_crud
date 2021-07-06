package com.example.backent.payload;

import com.example.backent.entity.enums.AttachmentType;
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
  private AttachmentType fileType;
  private long size;
}
