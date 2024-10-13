package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.PaymentMethod;

import java.util.List;

public interface PaymentService {

    List<Payment> getMyPayments();

    List<PaymentMethod> paymentMethodList();
}
