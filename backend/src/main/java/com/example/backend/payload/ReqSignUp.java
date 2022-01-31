package com.example.backend.payload;

import com.example.backend.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSignUp {
    private Long id;

    @NotNull
    @NotBlank()
    @Pattern(regexp = "[A-z]*", message = "firstname is invalid")
    private String firstname;

    @Pattern(regexp = "[A-z]*", message = "lastname is invalid")
    @NotNull
    @NotBlank()
    private String lastname;

    private String middleName;

    @Pattern(
            regexp = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$",
            message = "date Of Birthis invalid yyyy-MM-dd")
    private String dateOfBirth;

    private Gender gender; // REQUIRED

    private String nation;

    private String addressOfBirth;

    private String citizenship; // fiqaroligi


    @Pattern(
            regexp = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$",
            message = "date Of passportGivenTime invalid yyyy-MM-dd")
    private String passportGivenTime;

    private String passportWhoGave;

    @NotNull
    @NotBlank()
    @Pattern(regexp = "[A-Z]{2}[0-9]{7}", message = "passport number is invalid")
    private String passportNumber;


    @NotNull
    @NotBlank()
    @Pattern(
            regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\\\s\\\\./0-9]*$",
            message = "telephone number is invalid")
    private String phoneNumber;

    private Long photoId;

}
