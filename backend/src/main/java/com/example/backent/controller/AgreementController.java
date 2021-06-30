package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqAgreement;
import com.example.backent.payload.ReqProject;
import com.example.backent.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agreement")
public class AgreementController {

    @Autowired
    AgreementService agreementService;

    @PostMapping
    public HttpEntity<?> addProject(@RequestBody ReqAgreement reqAgreement){
        ApiResponseModel response = agreementService.addAgreement(reqAgreement);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll(){
        ApiResponseModel response = agreementService.getAllAgreement();
        return ResponseEntity.ok(response);
    }
}
