package com.example.backent.payload;

import com.example.backent.entity.UserExperience;
import com.example.backent.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSignUp {
  private String firstname;
  private String lastname;
  private String middleName;
  private String address;
  private String phoneNumber;
  private Date dateOfBirth;
  private String passportNumber;
  private String password;
  private String email;
  private List<Long> language;
  private List<Long> fields;
  private List<UserExperience> experiences; // gson
  private List<Long> programingLanguages;
  private RoleName roles;
}
