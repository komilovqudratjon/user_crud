package com.example.backent.repository;

import com.example.backent.entity.Board;
import com.example.backent.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByProjectId(Long project_id);
}
