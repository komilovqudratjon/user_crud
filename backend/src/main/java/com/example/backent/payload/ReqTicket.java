package com.example.backent.payload;

import com.example.backent.entity.ProjectType;
import com.example.backent.entity.enums.TicketCondition;
import com.example.backent.entity.enums.WorkType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ReqTicket {
  private Long id;
  private WorkType workType;
  @NotNull @NotBlank() private String text;
  private Long workerId; //
  private Long pmId; //
  private Long testerId; //
  private Long hoursWorker;
  private Long hoursTester;
  private Long board; //
  private Long programingLanguage; //
  private Long completeQuestion; //
  private TicketCondition ticketCondition; //
  private Boolean deleted;
  private Long type;
}
