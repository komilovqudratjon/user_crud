package com.example.backent.entity;

import com.example.backent.entity.enums.TimeType;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserExperience extends AbsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public UserExperience(String name, Long time, TimeType timeType) {
    this.name = name;
    this.time = time;
    this.timeType = timeType;
  }

  private String name;
  private Long time;
  private TimeType timeType;
}
