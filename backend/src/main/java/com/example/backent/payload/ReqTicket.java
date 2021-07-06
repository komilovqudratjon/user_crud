package com.example.backent.payload;

import com.example.backent.entity.ProjectType;
import com.example.backent.entity.enums.WorkType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ReqTicket {
  private Long id;
  private String workType;
  private String text;
  private Long workerId;
  private Long pmId;
  private Long testerId;
  private Long hoursWorker;
  private Long hoursTester;
  private Long board;
  private Long project;
  private Long programingLanguage;
  private Long completeQuestion;
  private Boolean deleted;
  private Long type;
}
