package com.example.backent.payload;

import com.example.backent.entity.UserExperience;
import com.example.backent.entity.enums.Family;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.entity.enums.WorkTimeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

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

  @NotNull @NotBlank() private String middleName;

  private String address;

  @NotNull
  @NotBlank()
  @Pattern(
      regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$",
      message = "telephone number is invalid")
  private String phoneNumber;

  @Pattern(
      regexp = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$",
      message = "date Of Birthis invalid")
  private String dateOfBirth;

  @NotNull
  @NotBlank()
  @Pattern(regexp = "[A-Z]{2}[0-9]{7}", message = "passport number is invalid")
  private String passportNumber;

  @Length(min = 4, max = 30, message = "password is invalid length is min = 4 max = 30")
  private String password;

  @Email() @NotNull @NotBlank() private String email;
  private boolean active;

  @Pattern(
      regexp = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$",
      message = "startWorkingTime is invalid")
  private String startWorkingTime;

  private WorkTimeType workTimeType;
  private Family family;
  private List<Long> languages;
  private Long photoId;
  @NotNull private List<Long> fields;
  private List<UserExperience> experiences; // gson
  private List<Long> programingLanguages;
  @NotNull private List<RoleName> roles;
}
