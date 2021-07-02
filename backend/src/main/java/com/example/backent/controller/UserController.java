package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqUser;
import com.example.backent.service.UserService;
import com.example.backent.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

  // **************  GET USER  ****************//
  @GetMapping("/users")
  public List<ReqUser> getUsers(
      @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
      @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
      @RequestParam(name = "search", defaultValue = "all") String search) {
    return userService.getAllUsers(page, size, search);
  }

  @GetMapping(params = {"sortBy", "page", "size"})
  public HttpEntity<?> findPaginated(
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size,
      @RequestParam("sortBy") Optional<String> sortBy,
      UriComponentsBuilder uriBuilder,
      HttpServletResponse response)
      throws IOException {
    return userService.getPageable(page, size, sortBy, uriBuilder, response);
  }

  @ExceptionHandler
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ApiResponseModel handleException(Exception e) {
    return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e);
  }
}
