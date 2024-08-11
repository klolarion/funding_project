package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import com.klolarion.funding_project.application.port.out.PaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    @Override
    public void sendToFunding(String fundingAccount, Long amount) {

    }

    @Override
    public List<Payment> myPayments() {
        return null;
    }

    @Override
    public List<PaymentMethodList> myPaymentMethosList(Long memberId) {
        return null;
    }
}
