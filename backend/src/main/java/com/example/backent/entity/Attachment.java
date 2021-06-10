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
    private String name;
    private String telegramURL;
    private String path;
    private String content;
    @Enumerated(EnumType.STRING)
    private AttachmentType attachmentType;
    private long size;

}
