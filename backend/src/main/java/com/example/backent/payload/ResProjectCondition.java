package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResProjectCondition {
    private Long id;
    private Double backend;
    private Double frontend;
//    private Long webfront;
    private Double design;
    private Long tz;
    private String startDate;
    private String endDate;
    private ResUser pm;
    private List<ResUser> backends;
    private List<ResUser> frontends;
    private List<ResUser> designs;
    private List<ResUser> testers;
    private List<ResUser> qas;
    private Long allPercentage;
}
