package com.example.backent.entity;

import com.example.backent.entity.enums.TicketCondition;
import com.example.backent.entity.enums.WorkType;
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
public class Ticket extends AbsEntity {

  @Enumerated(EnumType.STRING)
  private WorkType workType;

  private String text;

  @OneToOne private User worker;

  @OneToOne private User pm;

  @OneToOne private User tester;

  private Long hoursWorker;

  private Long hoursTester;

  @ManyToOne private Board board;

  @ManyToOne private ProgramingLanguage programingLanguage;

  @OneToOne private CompleteQuestion completeQuestion;

  private boolean deleted;

  @ManyToOne private ProjectType projectType;

  @Enumerated(EnumType.STRING)
  private TicketCondition ticketCondition;

  @ManyToOne private Tag tag;
}
