package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqAgreement;
import com.example.backent.payload.ReqProject;
import com.example.backent.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/api/agreement")
public class AgreementController {

  @Autowired AgreementService agreementService;

  @PostMapping("/{why}")
  public ApiResponseModel agreement(@PathVariable String why, MultipartHttpServletRequest request) {
    return agreementService.agreement(why, request);
  }

  @GetMapping("/getByProject/{id}")
  public ApiResponseModel getByProject(@PathVariable Long id) {
    return agreementService.getByProject(id);
  }
}
