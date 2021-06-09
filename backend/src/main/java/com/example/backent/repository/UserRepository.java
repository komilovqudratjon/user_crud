package com.example.backent.repository;


import com.example.backent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByPhoneNumber(String phoneNumber);

    Optional<User> getByEmail(String email);

    Optional<User> getByPassportNumber(String passportNumber);

    boolean existsByPhoneNumber(String phoneNumber);


}
