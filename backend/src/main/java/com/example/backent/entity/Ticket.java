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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private WorkType workType; // **

  @Column(length = 10000)
  private String text; // **

  @OneToOne private User worker; // **

  @OneToOne private User pm; // **

  @OneToOne private User tester; // **

  private Long hoursWorker; // **

  private Long hoursTester; // **

  @ManyToOne private Board board; // **

  @ManyToOne private ProgramingLanguage programingLanguage; // *

  @OneToOne private CompleteQuestion completeQuestion; // **

  private boolean deleted;

  @ManyToOne private ProjectType projectType; // **

  @Enumerated(EnumType.STRING)
  private TicketCondition ticketCondition; // **

  public Ticket(
      WorkType workType,
      String text,
      User worker,
      User pm,
      User tester,
      Long hoursWorker,
      Long hoursTester,
      Board board,
      ProgramingLanguage programingLanguage,
      CompleteQuestion completeQuestion,
      boolean deleted,
      ProjectType projectType,
      TicketCondition ticketCondition,
      Tag tag) {
    this.workType = workType;
    this.text = text;
    this.worker = worker;
    this.pm = pm;
    this.tester = tester;
    this.hoursWorker = hoursWorker;
    this.hoursTester = hoursTester;
    this.board = board;
    this.programingLanguage = programingLanguage;
    this.completeQuestion = completeQuestion;
    this.deleted = deleted;
    this.projectType = projectType;
    this.ticketCondition = ticketCondition;
    this.tag = tag;
  }

  @ManyToOne private Tag tag; //
}
