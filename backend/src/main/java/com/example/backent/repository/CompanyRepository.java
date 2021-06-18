package com.example.backent.repository;

import com.example.backent.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAllByDeleted(Boolean deleted);

    Company findByIdAndDeleted(Long id, boolean deleted);

    boolean existsByName(String name);
}
