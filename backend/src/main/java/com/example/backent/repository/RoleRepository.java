package com.example.backent.repository;

import com.example.backent.entity.Role;
import com.example.backent.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByNameIn(List<RoleName> asList);

    Optional<Role> findByName(RoleName roleAdmin);

    List<Role> findAllByName(RoleName roleSuperAdmin);
}
