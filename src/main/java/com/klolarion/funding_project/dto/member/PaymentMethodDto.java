package com.klolarion.funding_project.dto.member;

import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDto {
    private List<PaymentMethodList> paymentMethodLists;
    private List<PaymentMethod> paymentMethods;
    private PaymentMethodList mainPaymentMethod;
}
