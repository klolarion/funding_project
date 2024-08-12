package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    /*펀딩에 송금*/
//    void sendToFunding(String fundingAccount, Long amount);
//
//    /*내 결제 목록*/
//    List<Payment> myPayments();
//
//    /*내 결제수단 목록*/
//    List<PaymentMethodList> myPaymentMethosList(Long memberId);
}
