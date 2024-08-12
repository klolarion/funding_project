package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
