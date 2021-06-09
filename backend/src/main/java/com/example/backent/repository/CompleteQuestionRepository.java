package com.example.backent.repository;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.CompleteQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompleteQuestionRepository extends JpaRepository<CompleteQuestion, Long> {

}
