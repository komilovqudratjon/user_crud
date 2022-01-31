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
public class ErrorsField {
    private String field;
    private String expelling;
}
