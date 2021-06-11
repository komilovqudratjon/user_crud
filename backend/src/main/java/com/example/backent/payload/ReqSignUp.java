package com.example.backent.payload;

import lombok.Data;

import java.util.Date;

@Data
public class ReqSignUp {
    private String firstname;
    private String lastname;
    private String middleName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String passportNumber;
    private String password;
    private String email;
}
