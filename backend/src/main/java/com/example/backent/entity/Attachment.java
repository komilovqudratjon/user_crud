package com.example.backent.entity;

import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attachment extends AbsEntity {
    private String name;
    private String contentType;
    private long size;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AttachmentContent attachmentContent;

    public Attachment(String name, String contentType, long size) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
    }
}
