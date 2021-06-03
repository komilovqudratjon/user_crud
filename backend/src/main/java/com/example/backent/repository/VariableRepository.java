package com.example.backent.repository;

import com.example.backent.entity.Variable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariableRepository extends JpaRepository<Variable, Long> {
}
