package com.example.backent.payload;

import com.example.backent.entity.Company;
import com.example.backent.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResProject {
    private Long id;
    private String name;
    private ProjectType type;
    private Company company;
    private List<String> agreement;
}
