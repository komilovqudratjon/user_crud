package com.example.backent.repository;

import com.example.backent.entity.Board;
import com.example.backent.entity.Project;
import com.example.backent.entity.enums.BoardCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByProjectId(Long project_id);

    Optional<Board> findByProjectIdAndCondition(Long project_id, BoardCondition condition);

}
