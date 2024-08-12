package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.service.blueprint.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Override
    public List<Payment> getMyPayments() {
        return null;
    }
}
