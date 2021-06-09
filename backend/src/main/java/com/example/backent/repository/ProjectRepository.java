package com.example.backent.repository;

import com.example.backent.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Board, Long> {

}
