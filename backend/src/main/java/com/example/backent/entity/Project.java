package com.example.backent.entity;

import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project extends AbsEntity {

  private String name; // REQUIRED

  @ManyToOne private Company company;

  @OneToMany private List<Agreement> agreementList;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "project_users",
          joinColumns = {@JoinColumn(name = "project_id")},
          inverseJoinColumns = {@JoinColumn(name = "users_id")})
  private List<User> users;

  private String startDate;
  private String endDate;

  private boolean deleted;
}
