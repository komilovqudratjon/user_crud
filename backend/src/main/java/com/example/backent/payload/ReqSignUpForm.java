package com.example.backent.payload;

import com.example.backent.entity.enums.Gender;
import com.example.backent.entity.enums.Nation;
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

  @Pattern(
          regexp = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$",
          message = "date Of Birthis invalid")
  private String dateOfBirth;

  private Gender gender; // REQUIRED

  private String nation;

  private String addressOfBirth;

  private String citizenship; // fiqaroligi


  @Pattern(
          regexp = "^\\d\\d\\d\\d-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$",
          message = "date Of Birthis invalid")
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
