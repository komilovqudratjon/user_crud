package com.example.backent.entity;

import com.example.backent.entity.enums.BoardCondition;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board extends AbsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne private Project project;

  private boolean deleted;

  private Long index;

  @Enumerated(EnumType.STRING)
  private BoardCondition condition;

  public Board(
      String name, Project project, boolean deleted, Long index, BoardCondition condition) {
    this.name = name;
    this.project = project;
    this.deleted = deleted;
    this.index = index;
    this.condition = condition;
  }

  public Board(String name, Project project, Long index, BoardCondition condition) {
    this.name = name;
    this.project = project;
    this.index = index;
    this.condition = condition;
  }
}
