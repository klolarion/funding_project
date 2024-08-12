package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupStatusRepository extends JpaRepository<GroupStatus, Long> {
}
