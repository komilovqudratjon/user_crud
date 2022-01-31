package com.example.backend.controller;

import com.example.backend.payload.ApiResponseModel;
import com.example.backend.payload.ErrorsField;
import com.example.backend.payload.ReqSignUp;
import com.example.backend.payload.ReqSignUpForm;
import com.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.text.ParseException;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    AuthService authService;

    // **************** REGISTER USER ****************//
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

    // **************** REGISTER USER ****************//
    @PostMapping("/registerForm")
    @Validated
    public HttpEntity<?> registerForm(@Valid @RequestBody ReqSignUpForm reqSignUp, BindingResult error)
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

        return authService.registerForm(reqSignUp);
    }

    // **************** USER PHOTO UPLOAD ****************//
    @PostMapping("/uploadAvatar")
    public ApiResponseModel uploadFile(MultipartHttpServletRequest request) {
        return authService.uploadPhotoFileList(request);
    }

    // **************** USER DELETE ****************//
    @DeleteMapping("/deleteUser/{id}")
    public HttpEntity<?> deleteUser(@PathVariable Long id) {
        return authService.deleteUser(id);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponseModel handleException(Exception e) {
        return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.hashCode());
    }
}
