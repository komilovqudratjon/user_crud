package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/attach")
public class AttachmentController {

  @Autowired AttachmentService attachmentService;

  @GetMapping("/{id}")
  public HttpEntity<?> getFile(@PathVariable Long id) throws IOException {
    return attachmentService.getFile(id);
  }

  // **************** PHOTO UPLOAD ****************//
  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping("/uploadFile")
  public ApiResponseModel uploadFile(MultipartHttpServletRequest request) {
    return attachmentService.uploadFile(request);
  }
}
