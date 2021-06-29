package com.example.backent.payload;

import com.example.backent.entity.UserExperience;
import com.example.backent.entity.enums.Family;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.entity.enums.WorkTimeType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSignUp {
  @NotNull() private String firstname;
  @NotNull private String lastname;
  private String middleName;
  private String address;
  @NotNull private String phoneNumber;
  private Date dateOfBirth;
  private String passportNumber;
  @NotNull private String password;
  @Email private String email;
  private boolean active;
  private Date startWorkingTime;
  private WorkTimeType workTimeType;
  private Family family;
  private List<Long> language;
  private Long photoId;
  @NotNull private List<Long> fields;
  private List<UserExperience> experiences; // gson
  private List<Long> programingLanguages;
  private List<RoleName> roles;
}
