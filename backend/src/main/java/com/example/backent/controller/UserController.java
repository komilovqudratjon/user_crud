package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
  public HttpEntity<?> findPaginated(
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size,
      @RequestParam("sortBy") Optional<String> sortBy,
      @RequestParam("search") Optional<String> search) {
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
