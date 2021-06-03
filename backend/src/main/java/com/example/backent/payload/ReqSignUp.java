package com.example.backent.payload;

import lombok.Data;

@Data
public class ReqSignUp {
    private Long  id;
    private String firstname;
    private String lastname;
    private String middleName;
    private String phoneNumber;
    private String dateOfBirth;
    private String passportNumber;
    private String email;
    private int category;
    private String role;
}
