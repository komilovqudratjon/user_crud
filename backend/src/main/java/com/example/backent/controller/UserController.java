package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ErrorsField;
import com.example.backent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
@RequestMapping("/api/user")
public class UserController {

  @Autowired UserService userService;

  // **************** SAVE LANGUAGE ****************//
  @PostMapping("/language/{language}")
  public ApiResponseModel saveLanguage(@PathVariable String language) {
    return userService.saveLanguage(language);
  }

  // **************** GET LANGUAGE ****************//
  @GetMapping("/language")
  public ApiResponseModel getLanguage() {
    return userService.getLanguage();
  }

  // **************** DELETE LANGUAGE ****************//
  @DeleteMapping("/language/{id}")
  public ApiResponseModel deleteLanguage(@PathVariable Long id) {
    return userService.deleteLanguage(id);
  }

  // **************** SAVE FIELDS ****************//
  @PostMapping("/fields/{fields}")
  public ApiResponseModel saveFields(@PathVariable String fields) {
    return userService.saveFields(fields);
  }

  // **************** GET FIELDS ****************//
  @GetMapping("/fields")
  public ApiResponseModel getFields() {
    return userService.getFields();
  }

  // **************** DELETE FIELDS ****************//
  @DeleteMapping("/fields/{id}")
  public ApiResponseModel deleteDelete(@PathVariable Long id) {
    return userService.deleteDelete(id);
  }

  // **************** GET USER BY ID ****************//
  @GetMapping("/getUserById/{id}")
  public HttpEntity<?> getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  // **************  GET USER  ****************//
  @GetMapping()
  //  @Validated
  public HttpEntity<?> findPaginated(
      @Valid @RequestParam(value = "page", defaultValue = "0") @Min(0) Optional<Integer> page,
      @Valid @RequestParam(value = "size", defaultValue = "5") @Min(0) Optional<Integer> size,
      @Valid @RequestParam(value = "sortBy", defaultValue = "id") Optional<String> sortBy,
      @Valid @RequestParam("search") Optional<String> search) {
    //    if (error.hasErrors()) {
    //      return ResponseEntity.status(HttpStatus.CONFLICT)
    //          .body(
    //              new ApiResponseModel(
    //                  HttpStatus.CONFLICT.value(),
    //                  "field",
    //                  error.getFieldErrors().stream()
    //                      .map(
    //                          fieldError ->
    //                              new ErrorsField(
    //                                  fieldError.getField(), fieldError.getDefaultMessage()))));
    //    }

    return userService.getPageable(page, size, sortBy, search);
  }

  // **************  GET USER  ****************//
  @GetMapping("/document")
  public Object document(
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size,
      @RequestParam("sortBy") Optional<String> sortBy) {
    return userService.document(page, size, sortBy);
  }

  @ExceptionHandler
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ApiResponseModel handleException(Exception e) {
    return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
  }
}
