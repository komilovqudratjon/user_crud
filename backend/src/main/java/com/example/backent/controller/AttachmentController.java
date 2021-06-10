package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@RestController
@RequestMapping("/attach")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @PostMapping
    public HttpEntity<?> uploadFile(MultipartHttpServletRequest request){
        ApiResponseModel apiResponseModel = attachmentService.uploadFile(request);
        return ResponseEntity.status(200).body("cjbhwhhckwhkjchkjw");
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable Long id) throws IOException {
        return attachmentService.getFile(id);
    }

}
