package com.example.backent.repository;

import com.example.backent.entity.Board;
import com.example.backent.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByDeleted(boolean deleted);
}
