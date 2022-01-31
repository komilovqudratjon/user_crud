package com.example.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceException {
    private int status;
    private String message;
    private Object fieldValue;
}
