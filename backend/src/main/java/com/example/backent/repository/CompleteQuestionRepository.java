package com.example.backent.repository;

import com.example.backent.entity.CompleteQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CompleteQuestionRepository extends JpaRepository<CompleteQuestion, Long> {

}
