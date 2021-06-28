package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResCompleteQuestion {
    private Long id;
    private String photo;
    private String text;
    private String link;
}
