package com.example.backent.repository;

import com.example.backent.entity.Role;
import com.example.backent.entity.User;
import com.example.backent.entity.enums.RoleName;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> getByPhoneNumber(String phoneNumber);

  Optional<User> getByEmail(String email);

  Optional<User> getByPassportNumber(String passportNumber);

  List<User>
      findAllByLastnameContainingOrMiddlenameContainingOrFirstnameContainingOrPhoneNumberContainingOrPassportNumberContainingOrEmailContainingOrAddressContaining(
          Pageable pageable,
          String lastname,
          String middlename,
          String firstname,
          String phoneNumber,
          String passportNumber,
          String email,
          String address);

  boolean existsByEmail(String phoneNumber);
}
