package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResAgreement {
    private Long id;
    private String why;
    private String file;
}
