package com.example.backent.repository;

import com.example.backent.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

  List<Attachment> findAllByIdIn(Collection<Long> id);
}
