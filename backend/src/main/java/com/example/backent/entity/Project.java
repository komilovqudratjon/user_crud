package com.example.backent.entity;

import com.example.backent.entity.enums.ProjectSTATUS;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project extends AbsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name; // REQUIRED

  @ManyToOne private Company company;

  @OneToMany private List<Agreement> agreementList;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "project_users",
      joinColumns = {@JoinColumn(name = "project_id")},
      inverseJoinColumns = {@JoinColumn(name = "users_id")})
  private List<User> users;

  private Date startDate;
  private Date endDate;

  private boolean deleted;
  private ProjectSTATUS projectType; //

  public Project(
      String name,
      Company company,
      List<Agreement> agreementList,
      List<User> users,
      Date startDate,
      Date endDate,
      boolean deleted,
      ProjectSTATUS projectType,
      User responsible) {
    this.name = name;
    this.company = company;
    this.agreementList = agreementList;
    this.users = users;
    this.startDate = startDate;
    this.endDate = endDate;
    this.deleted = deleted;
    this.projectType = projectType;
    this.responsible = responsible;
  }

  @ManyToOne private User responsible;
}
