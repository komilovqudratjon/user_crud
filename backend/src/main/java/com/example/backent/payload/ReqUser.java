package com.example.backent.payload;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.Family;
import com.example.backent.entity.enums.WorkTimeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqUser {
  private Long id;

  private String firstname;

  private String lastname;

  private String middlename;

  private String address;

  private WorkTimeType workTimeType;

  private Family family;

  private String passportNumber;

  private Date dateOfBirth;

  private Date startWorkingTime;

  private String phoneNumber;

  private String email;

  private List<ReqIdAndName> fields;

  private List<UserExperience> experiences;

  private List<ReqIdAndName> languages;

  private List<ProgramingLanguage> programingLanguages;

  private String avatar;

  private List<Role> roles;
}
