package com.example.backent.payload;

import com.example.backent.entity.UserExperience;
import com.example.backent.entity.enums.Family;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.entity.enums.WorkTimeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSignUp {
  @NotNull @NotBlank() private String firstname;
  @NotNull @NotBlank() private String lastname;
  @NotNull @NotBlank() private String middleName;
  private String address;
  @NotNull @NotBlank() private String phoneNumber;
  private Date dateOfBirth;
  @NotNull @NotBlank() private String passportNumber;
  @NotNull @NotBlank() private String password;
  @Email() @NotNull @NotBlank() private String email;
  private boolean active;
  private Date startWorkingTime;
  private WorkTimeType workTimeType;
  private Family family;
  private List<Long> language;
  private Long photoId;
  @NotNull @NotBlank() private List<Long> fields;
  private List<UserExperience> experiences; // gson
  private List<Long> programingLanguages;
  private List<RoleName> roles;
}
