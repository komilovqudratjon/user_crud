package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseModel {
    private Integer statusCode;
    private String message;
    private Object data;

    public ApiResponseModel(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
