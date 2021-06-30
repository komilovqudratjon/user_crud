package com.example.backent.repository;

import com.example.backent.entity.CompleteQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CompleteQuestionRepository extends JpaRepository<CompleteQuestion, Long> {
    List<CompleteQuestion> findAllByDeleted(boolean deleted);
    Optional<CompleteQuestion> findByIdAndDeleted(Long id, boolean deleted);
}
