package com.example.backent.repository;

import com.example.backent.entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTypeRepository extends JpaRepository<ProjectType,Long> {

    boolean existsByNameAndDeleted(String name,boolean deleted);

    Optional<ProjectType> findByIdAndDeleted(Long id, boolean deleted);

    List<ProjectType> findAllByDeleted(boolean deleted);
}
