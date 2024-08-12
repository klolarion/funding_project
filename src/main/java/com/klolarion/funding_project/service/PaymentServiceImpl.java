package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Payment;

import java.util.List;

public class PaymentServiceImpl implements PaymentService{
    @Override
    public List<Payment> getAllPayments() {
        return null;
    }

    @Override
    public boolean changeComplete(Long paymentId, boolean completeStatus) {
        return false;
    }

    @Override
    public List<Payment> getMyPayments() {
        return null;
    }
}
