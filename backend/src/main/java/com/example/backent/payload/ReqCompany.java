package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCompany {
  private Long id;
  private String name;
  private String responsiblePerson;
  private Long balance;
  private Long oked;
  private Long mfo;
  private Long stir;
  private String phoneNumber;
  private String email;
  private String address;
  private List<Long> deleteFile;
}
