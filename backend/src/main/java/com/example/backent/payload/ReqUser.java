package com.example.backent.payload;

import lombok.Data;

@Data
public class ReqUser {
  private Long userId;
  private int category;
  private String lastname;
  private String firstname;
  private String middlename;
  private String email;
}
