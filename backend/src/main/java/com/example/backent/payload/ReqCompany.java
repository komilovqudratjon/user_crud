package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCompany {
  private Long id;

  @NotNull
  @NotBlank()
  @Pattern(regexp = "[A-z]*", message = "name is invalid")
  private String name;

  @Pattern(regexp = "[A-z]*", message = "responsiblePerson is invalid")
  private String responsiblePerson;

  private Long balance;
  private Long oked;
  private Long mfo;
  private Long stir;

  @NotNull
  @NotBlank()
  @Pattern(
      regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$",
      message = "phoneNumber number is invalid")
  private String phoneNumber;

  @Email() private String email;

  @Pattern(regexp = "[A-z]*", message = "address is invalid")
  private String address;

  private List<Long> agreement;
}
