package com.example.backent.entity;

import com.example.backent.entity.enums.AttachmentType;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attachment extends AbsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name; //
  private String telegramURL;
  private String path;
  private String contentType; //
  private String extension;

  @Enumerated(EnumType.STRING)
  private AttachmentType attachmentType; //

  private long size; //
}
