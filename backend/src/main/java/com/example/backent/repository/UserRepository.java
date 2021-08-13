package com.example.backent.repository;

import com.example.backent.entity.Role;
import com.example.backent.entity.User;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.payload.ResUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> getByEmailAndActive(String email, boolean active);

  Page<User> findAllByDeleted(boolean deleted, Pageable pageable);

  Page<User>
      findAllByDeletedAndFirstnameContainingOrLastnameContainingOrMiddlenameContainingOrAddressContainingOrPassportNumberContainingOrEmailContaining(
          boolean deleted,
          String firstname,
          String lastname,
          String middlename,
          String address,
          String passportNumber,
          String email,
          Pageable pageable);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, Long id);

  boolean existsByPassportNumber(String passportNumber);

  boolean existsByPassportNumberAndIdNot(String passportNumber, Long id);

  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
}
