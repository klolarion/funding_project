package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
