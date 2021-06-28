package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResUser {
    private Long id;
    private String name;
    private String email;
    private String avatar;
}
