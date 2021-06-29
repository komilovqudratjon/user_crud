package com.example.backent.repository;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
  List<Agreement> findAllByIdIn(Collection<Long> id);
}
