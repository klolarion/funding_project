package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.PaymentMethod;

public interface PaymentMethodService {
    PaymentMethod addPaymentMethod(int code, String paymentName, String accountNumber, Long availableAmount);
    boolean deletePaymentMethod(Long paymentMethodId);
}
