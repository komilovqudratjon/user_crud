package com.example.backent.service;

import com.example.backent.entity.Attachment;
import com.example.backent.payload.ApiResponseModel;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AttachmentService {

  @Value("${path}")
  private String upload;

  @Autowired AttachmentRepository attachmentRepository;

  public ApiResponseModel uploadFile(MultipartHttpServletRequest request) {
    ApiResponseModel response = new ApiResponseModel();
    Iterator<String> iterator = request.getFileNames();
    MultipartFile file1;
    while (iterator.hasNext()) {
      file1 = request.getFile(iterator.next());
      Calendar calendar = Calendar.getInstance();
      try {
        File file =
            new File(
                "D:\\rasmlar/" + calendar.get(Calendar.DATE) + "/" + file1.getOriginalFilename());
        file.mkdirs();
        file1.transferTo(file);
        Attachment attachment = new Attachment();
        attachment.setName(file1.getOriginalFilename());
        attachment.setContent(file1.getContentType());
        attachment.setSize(file1.getSize());
        attachment.setPath(file.getPath());
        attachmentRepository.save(attachment);
      } catch (Exception e) {
        response.setMessage("error !");
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      }
    }

    return response;
  }

  public HttpEntity<?> getFile(Long id) throws IOException {
    Optional<Attachment> attachment = attachmentRepository.findById(id);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(attachment.get().getContent()))
        .body(Files.readAllBytes(Paths.get(attachment.get().getPath())));
  }
}
