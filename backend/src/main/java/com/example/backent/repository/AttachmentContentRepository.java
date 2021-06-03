package com.example.backent.repository;

import com.example.backent.entity.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Long> {
}
