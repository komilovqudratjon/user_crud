package com.example.backend.controller;

import com.example.backend.payload.ApiResponseModel;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    // **************** GET USER BY ID ****************//
    @GetMapping("/getUserById/{id}")
    public HttpEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // **************  GET USERS  ****************//
    @GetMapping()
    public HttpEntity<?> findPaginated(
            @Valid @RequestParam(value = "page", defaultValue = "0") @Min(0) Optional<Integer> page,
            @Valid @RequestParam(value = "size", defaultValue = "5") @Min(0) Optional<Integer> size,
            @Valid @RequestParam(value = "sortBy", defaultValue = "id") Optional<String> sortBy,
            @Valid @RequestParam(value = "search", defaultValue = "") Optional<String> search) {


        return userService.getPageable(page, size, sortBy, search);
    }


    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponseModel handleException(Exception e) {
        return new ApiResponseModel(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }
}
