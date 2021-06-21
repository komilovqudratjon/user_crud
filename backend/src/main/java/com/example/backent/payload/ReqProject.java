package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqProject {

  private Long id;
  private String name;
  private Long companyId;
  private Long type;
  private List<Long> agreements;
}
