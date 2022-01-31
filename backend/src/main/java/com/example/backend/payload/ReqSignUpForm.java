package com.example.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSignUpForm {
    private Long id;

    private String currentStatus;

    private String susceptibilityToDisease;

    private String propensityToAssassinate;

    private String weaknessesAndStrengths;

    private String socialResponsibility;

    private List<Long> anotherPhotos;

}
