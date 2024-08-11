package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.domain.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InfraFundingRepository extends JpaRepository<Funding, Long> {

}
