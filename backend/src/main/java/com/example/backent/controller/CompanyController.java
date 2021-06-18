package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqCompany;
import com.example.backent.repository.CompanyRepository;
import com.example.backent.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping
    public HttpEntity<?> addCompany(@RequestBody ReqCompany reqCompany){
        ApiResponseModel response = companyService.addOrEditCompany(reqCompany);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteCompany(@PathVariable Long id){
        ApiResponseModel response = companyService.deleteCompany(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAllCompany(){
        ApiResponseModel response = companyService.getAllCompany();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/one/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id){
        ApiResponseModel response = companyService.getOneCompany(id);
        return ResponseEntity.ok(response);
    }

}
