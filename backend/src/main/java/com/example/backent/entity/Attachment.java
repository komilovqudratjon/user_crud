package com.example.backent.entity;

import com.example.backent.entity.enums.AttachmentCause;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attachment extends AbsEntity {
  private String name;
  private String telegramURL;
  private String path;
  private String extension;

  @Enumerated(EnumType.STRING)
  private AttachmentCause why;

  private long size;
}
