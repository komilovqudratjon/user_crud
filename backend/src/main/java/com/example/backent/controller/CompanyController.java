package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ErrorsField;
import com.example.backent.payload.ReqCompany;
import com.example.backent.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

  @Autowired CompanyService companyService;

  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping
  @Validated
  public HttpEntity<?> addCompany(@Valid @RequestBody ReqCompany reqCompany, BindingResult error) {
    if (error.hasErrors()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "field",
                  error.getFieldErrors().stream()
                      .map(
                          fieldError ->
                              new ErrorsField(
                                  fieldError.getField(), fieldError.getDefaultMessage()))));
    }

    return companyService.addOrEditCompany(reqCompany);
  }

  @DeleteMapping("/{id}")
  public HttpEntity<?> deleteCompany(@PathVariable Long id) {
    ApiResponseModel response = companyService.deleteCompany(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/all")
  public HttpEntity<?> getAllCompany() {
    ApiResponseModel response = companyService.getAllCompany();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/one/{id}")
  public HttpEntity<?> getOne(@PathVariable Long id) {
    ApiResponseModel response = companyService.getOneCompany(id);
    return ResponseEntity.ok(response);
  }
}
