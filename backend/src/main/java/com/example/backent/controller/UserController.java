package com.example.backent.controller;

import com.example.backent.entity.enums.RoleName;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.service.UserService;
import com.example.backent.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
@RequestMapping("/api/user")
public class UserController {

  @Autowired UserService userService;

  // **************** SAVE LANGUAGE ****************//
  @PostMapping("/language/{language}")
  public ApiResponseModel saveLanguage(@PathVariable String language) {
    System.out.println(language);
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

  // ************** GET USER

  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
  @GetMapping("/user")
  public HttpEntity<?> getUsers(
      @RequestParam(name = "startDate", defaultValue = AppConstants.BEGIN_DATE) String startTime,
      @RequestParam(name = "endDate", defaultValue = AppConstants.END_DATE) String endTime,
      @RequestParam(name = "roleName", defaultValue = "USER") RoleName roleName,
      @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
      @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
      @RequestParam(name = "search", defaultValue = "all") String search,
      @RequestParam(name = "active", defaultValue = "true") Boolean isActive) {
    return userService.getAllUsers(startTime, endTime, roleName, page, size, search, isActive);
  }

  @ExceptionHandler
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ApiResponseModel handleException(Exception e) {
    return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.hashCode());
  }
}
