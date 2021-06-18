package com.example.backent.entity;

import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class UsersLanguage extends AbsEntity {
  @Column(unique = true)
  private String name;
}
