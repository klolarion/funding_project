package com.klolarion.funding_project.dto.member;

import com.klolarion.funding_project.dto.PaymentMethodDto;
import com.klolarion.funding_project.dto.PaymentMethodListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPaymentMethodDto {
    private List<PaymentMethodListDto> paymentMethodLists;
    private List<PaymentMethodDto> paymentMethods;
    private PaymentMethodListDto mainPaymentMethod;
}
