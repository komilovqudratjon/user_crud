package com.example.backent.entity;

import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project extends AbsEntity {

  private String name;

  @ManyToOne private Company company;

  @OneToMany private List<Agreement> agreementList;

//  @ManyToOne private ProjectType type;

  private boolean deleted;
}
