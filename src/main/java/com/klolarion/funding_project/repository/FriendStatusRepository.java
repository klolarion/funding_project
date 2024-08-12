package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendStatusRepository extends JpaRepository<FriendStatus, Long> {
}
