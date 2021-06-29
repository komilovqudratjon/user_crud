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
