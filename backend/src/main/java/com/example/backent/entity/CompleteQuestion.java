package com.example.backent.entity;

import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompleteQuestion extends AbsEntity {

  @OneToOne private Attachment questionPhoto;

  private String text;

  private String link;
  private boolean deleted;
}
