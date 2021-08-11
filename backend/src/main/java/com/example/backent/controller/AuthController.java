package com.example.backent.controller;

import com.example.backent.entity.User;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ErrorsField;
import com.example.backent.payload.ReqSignIn;
import com.example.backent.payload.ReqSignUp;
import com.example.backent.security.AuthService;
import com.example.backent.security.CurrentUser;
import com.example.backent.security.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired JwtTokenProvider jwtTokenProvider;

  @Autowired AuthenticationManager authenticate;

  @Autowired AuthService authService;

  // **************** GET  YOURSELF  ****************//
  @GetMapping("/me")
  //  @ApiOperation(value = "Get list of Students in the System ", response = Iterable.class, tags =
  // "getStudents")
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "Authorization",
          value = "Access Token",
          required = true,
          allowEmptyValue = false,
          paramType = "header",
          dataTypeClass = String.class,
          example = "Bearer access_token"))
  public ApiResponseModel getUser(@ApiIgnore @CurrentUser User user) {
    return new ApiResponseModel(
        user != null ? 200 : 204, user != null ? "user info" : "Error", user);
  }

  // **************** REGISTER USER ****************//
  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping("/register")
  @Validated
  public HttpEntity<?> register(@Valid @RequestBody ReqSignUp reqSignUp, BindingResult error)
      throws ParseException {
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

    return authService.register(reqSignUp);
  }

  // **************** USER PHOTO UPLOAD ****************//
  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping("/uploadAvatar")
  public ApiResponseModel uploadFile(MultipartHttpServletRequest request) {
    return authService.uploadPhotoFileList(request);
  }

  // **************** USER DELETE ****************//
  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @DeleteMapping("/deleteUser/{id}")
  public HttpEntity<?> deleteUser(@ApiIgnore @CurrentUser User user, @PathVariable Long id) {
    return authService.deleteUser(id, user);
  }

  // **************** LOGIN USER ****************//
  @PermitAll
  @PostMapping("/login")
  public HttpEntity<?> login(@Valid @RequestBody ReqSignIn reqSignIn) {
    try {
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new ApiResponseModel(
                  HttpStatus.OK.value(),
                  "login",
                  authService
                      .getApiToken(reqSignIn.getEmail(), reqSignIn.getPassword())
                      .getBody()));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "error",
                  List.of(new ErrorsField("error", e.getMessage()))));
    }
  }

  @ExceptionHandler
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ApiResponseModel handleException(Exception e) {
    return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.hashCode());
  }
}
