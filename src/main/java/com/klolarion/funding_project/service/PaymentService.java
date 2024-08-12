package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Payment;

import java.util.List;

public interface PaymentService {

    //for admin
    List<Payment> getAllPayments();
    boolean changeComplete(Long paymentId, boolean completeStatus);


    List<Payment> getMyPayments();
}
