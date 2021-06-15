package com.example.backent.repository;

import com.example.backent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> getByPhoneNumber(String phoneNumber);

  Optional<User> getByEmail(String email);

  Optional<User> getByPassportNumber(String passportNumber);

  boolean existsByEmail(String phoneNumber);
}
