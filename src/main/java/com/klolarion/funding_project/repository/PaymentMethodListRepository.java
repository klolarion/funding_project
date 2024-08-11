package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodListRepository extends JpaRepository<PaymentMethodList, Long> {
}
