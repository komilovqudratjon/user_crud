package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqAgreement {
    private Long id;
    private String why;
    private Long fileId;
}
