package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseModel {

    private Integer code;
    private String message;
    private Object data;

    public ApiResponseModel(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
