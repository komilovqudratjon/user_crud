package com.example.backent.payload;

import lombok.Data;

@Data
public class ReqFullQuestion {
    private Long id;
    private Long photo;
    private String text;
    private String link;
}
