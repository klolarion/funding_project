package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderList, Long> {
}
