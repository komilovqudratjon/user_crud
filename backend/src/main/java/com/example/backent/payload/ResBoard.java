package com.example.backent.payload;

import com.example.backent.entity.enums.BoardCondition;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
public class ResBoard {
  private Long id;
  private String name;
  private Long projectId;
  private Long index;
  private BoardCondition condition;
}
