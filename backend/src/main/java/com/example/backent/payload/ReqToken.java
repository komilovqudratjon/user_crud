package com.example.backent.payload;

import uz.ssd.testapp.entity.Role;

import java.util.List;


public class ReqToken {
    private Long id;
    private String email;
    private List<Role> role;

    public ReqToken(Long id, String email, List<Role> role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public ReqToken() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
