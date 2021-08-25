package com.example.backent.service;

import com.example.backent.entity.Attachment;
import com.example.backent.entity.enums.AttachmentType;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ResUploadFile;
import com.example.backent.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AttachmentService {

  @Autowired AttachmentRepository attachmentRepository;

  @Value("${upload.folder}")
  private String path;

  public ApiResponseModel uploadFile(MultipartHttpServletRequest request) {
    try {

      Iterator<String> iterator = request.getFileNames();
      MultipartFile multipartFile;
      List<ResUploadFile> resUploadFiles = new ArrayList<>();
      if (iterator.hasNext()) {
        multipartFile = request.getFile(iterator.next());
        Attachment attachment = new Attachment();
        assert multipartFile != null;
        if (getExt(multipartFile.getOriginalFilename()) == null || multipartFile.getSize() == 0) {
          return new ApiResponseModel(HttpStatus.CONFLICT.value(), "file is invalid");
        }
        attachment.setContentType(multipartFile.getContentType());
        attachment.setSize(multipartFile.getSize());
        attachment.setName(multipartFile.getOriginalFilename());
        attachment.setAttachmentType(AttachmentType.PROFILE);
        attachment.setExtension(getExt(multipartFile.getOriginalFilename()));
        Attachment save = attachmentRepository.save(attachment);
        Calendar calendar = new GregorianCalendar();
        File uploadFolder =
            new File(
                path
                    + "/"
                    + calendar.get(Calendar.YEAR)
                    + "/"
                    + calendar.get(Calendar.MONTH)
                    + "/"
                    + calendar.get(Calendar.DAY_OF_MONTH));

        if (uploadFolder.mkdirs() && uploadFolder.exists()) {
          System.out.println("create folder for user profile " + uploadFolder.getAbsolutePath());
        }

        uploadFolder = uploadFolder.getAbsoluteFile();
        File file =
            new File(
                uploadFolder
                    + "/"
                    + save.getId()
                    + "_"
                    + attachment.getName()
                    + save.getExtension());
        save.setPath(file.getAbsolutePath());
        multipartFile.transferTo(file);

        Attachment save1 = attachmentRepository.save(save);
        resUploadFiles.add(
            new ResUploadFile(
                save1.getId(),
                save1.getName(),
                ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("api/attach/")
                    .path(attachment.getId().toString())
                    .toUriString(),
                save1.getAttachmentType(),
                save1.getSize()));
        return new ApiResponseModel(HttpStatus.OK.value(), "saved", resUploadFiles);
      }
      return new ApiResponseModel(HttpStatus.OK.value(), "not saved", null);

    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage(), null);
    }
  }

  public String getExt(String fileName) {
    String ext = null;
    if (fileName != null && !fileName.isEmpty()) {
      int dot = fileName.lastIndexOf(".");
      if (dot > 0 && dot <= fileName.length() - 2) {
        ext = fileName.substring(dot);
      }
    }
    return ext;
  }

  public HttpEntity<?> getFile(Long id) throws IOException {
    Optional<Attachment> attachment = attachmentRepository.findById(id);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(attachment.get().getContentType()))
        .body(Files.readAllBytes(Paths.get(attachment.get().getPath())));
  }
}
