package com.springsecurity.repository;

import java.util.Optional;

import com.springsecurity.constant.RoleName;
import com.springsecurity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
