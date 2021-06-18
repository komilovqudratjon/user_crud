package com.example.backent.repository;

import com.example.backent.entity.UsersLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserLanguageRepository extends JpaRepository<UsersLanguage, Long> {
  List<UsersLanguage> findAllByIdIn(Collection<Long> id);
}
