package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.dto.payment.PaymentDto;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getMyPayments();

    List<PaymentMethod> paymentMethodList();
}
