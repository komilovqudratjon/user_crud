package com.example.backent.payload;

import com.example.backent.entity.Tag;
import com.example.backent.entity.enums.WorkType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResTicket {
    private Long id;
    private WorkType workType;
    private String text;
    private ResUser worker;
    private ResUser pm;
    private ResUser tester;
    private Long workerHour;
    private Long testerHour;
//    private Long board;
    private ResLanguage language;
    private ResCompleteQuestion fullQuestion;
    private Tag tag;
}
