package com.example.backent.entity;

import com.example.backent.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Company extends AbsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public Company(
      String name,
      String responsiblePerson,
      Long balance,
      Long oked,
      Long mfo,
      Long stir,
      String phoneNumber,
      String email,
      String address,
      List<Agreement> agreement,
      boolean deleted) {
    this.name = name;
    this.responsiblePerson = responsiblePerson;
    this.balance = balance;
    this.oked = oked;
    this.mfo = mfo;
    this.stir = stir;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.address = address;
    this.agreement = agreement;
    this.deleted = deleted;
  }

  private String name; // REQUIRED
  private String responsiblePerson;
  private Long balance;
  private Long oked;
  private Long mfo;
  private Long stir;
  private String phoneNumber; // REQUIRED
  private String email;
  private String address;

  @OneToMany private List<Agreement> agreement;

  private boolean deleted;
}
