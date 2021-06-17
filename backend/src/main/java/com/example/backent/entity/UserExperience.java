package com.example.backent.entity;

import com.example.backent.entity.enums.TimeType;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserExperience extends AbsEntity {
  private String name;
  private Long time;
  private TimeType timeType;
}
