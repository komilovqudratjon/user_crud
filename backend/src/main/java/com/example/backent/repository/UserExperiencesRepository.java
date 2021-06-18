package com.example.backent.repository;

import com.example.backent.entity.UserExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserExperiencesRepository extends JpaRepository<UserExperience, Long> {
  List<UserExperience> findAllByIdIn(Collection<Long> id);
}
