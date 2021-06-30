package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResCompany {
    private Long id;
    private String name; // REQUIRED
    private String responsiblePerson;
    private Long balance;
    private Long oked;
    private Long mfo;
    private Long stir;
    private String phoneNumber; // REQUIRED
    private String email;
    private String address;

    private List<ResAgreement> agreements;

}
