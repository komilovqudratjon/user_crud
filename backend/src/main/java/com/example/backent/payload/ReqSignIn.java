package com.example.backent.payload;

import lombok.Data;

@Data
public class ReqSignIn {
    private String email;
    private String password;
}
