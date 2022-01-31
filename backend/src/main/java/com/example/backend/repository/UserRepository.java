package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    List<User> findAllByIdIn(Collection<Long> id);

    Page<User>
    findAllByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingOrMiddleNameIgnoreCaseContainingOrAddressOfBirthIgnoreCaseContainingOrPinflIgnoreCaseContaining(
            String firstname,
            String lastname,
            String middlename,
            String address,
            String passportNumber,
            Pageable pageable);


    boolean existsByPinfl(String passportNumber);

    boolean existsByPinflAndIdNot(String passportNumber, Long id);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
}
