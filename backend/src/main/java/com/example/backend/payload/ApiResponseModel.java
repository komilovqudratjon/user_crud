package com.example.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
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
