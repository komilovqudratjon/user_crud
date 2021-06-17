package com.example.backent.repository;

import com.example.backent.entity.FieldsForUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserFieldsRepository extends JpaRepository<FieldsForUsers, Long> {

  List<FieldsForUsers> findAllByIdIn(Collection<Long> id);
}
