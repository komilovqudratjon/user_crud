package com.example.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Data
@AllArgsConstructor
public class ResUser {
    private Long id;
    private String name;
    private String email;
    private String avatar;
}
