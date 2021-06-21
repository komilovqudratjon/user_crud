package com.example.backent.repository;

import com.example.backent.entity.ProgramingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<ProgramingLanguage, Long> {
  List<ProgramingLanguage> findAllByDeleted(Boolean deleted);
}
