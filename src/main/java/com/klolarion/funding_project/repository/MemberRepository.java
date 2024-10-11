package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByNickName(String username);
}
