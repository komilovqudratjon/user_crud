package com.example.backent.entity;

import com.example.backent.entity.enums.BoardCondition;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board extends AbsEntity {

  private String name;

  @ManyToOne private Project project;

  private boolean deleted;

  private Long index;

  @Enumerated(EnumType.STRING)
  private BoardCondition condition;


}
