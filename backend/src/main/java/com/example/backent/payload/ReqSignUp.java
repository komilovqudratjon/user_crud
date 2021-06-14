package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
