package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.CodeMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<CodeMaster, Long> {
}
