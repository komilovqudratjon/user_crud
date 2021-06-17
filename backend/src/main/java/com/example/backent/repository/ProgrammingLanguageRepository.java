package com.example.backent.repository;

import com.example.backent.entity.ProgramingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProgrammingLanguageRepository extends JpaRepository<ProgramingLanguage, Long> {
  List<ProgramingLanguage> findAllByIdIn(Collection<Long> id);
}
