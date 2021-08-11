package com.example.backent.payload;

import com.example.backent.entity.Project;
import com.example.backent.entity.enums.BoardCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqBoard {
  private Long id;
  private String name;
  private Long project;
  private BoardCondition condition;
  private Long index;
}
