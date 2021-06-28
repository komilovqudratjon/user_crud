package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResBoard {
    private Long id;
    private String name;
    private Long projectId;
}
