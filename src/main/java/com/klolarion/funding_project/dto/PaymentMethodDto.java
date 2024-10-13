package com.klolarion.funding_project.dto;

import com.klolarion.funding_project.domain.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDto {
    private Integer paymentCode;
    private Long paymentMethodId;
    private String paymentName;
    private String accountNumber;
    private Long availableAmount;


    public static PaymentMethodDto fromDomainToPaymentMethodDto(PaymentMethod paymentMethod) {
        return new PaymentMethodDto(
                paymentMethod.getPaymentCode(),
                paymentMethod.getPaymentMethodId(),
                paymentMethod.getPaymentName(),
                paymentMethod.getAccountNumber(),
                paymentMethod.getAvailableAmount()
        );
    }

    public static List<PaymentMethodDto> fromDomainListToPaymentMethodDtoList(List<PaymentMethod> paymentMethods){
        if (paymentMethods != null) {
            // List<PaymentMethodList>를 List<PaymentMethodListDto>로 변환
            return paymentMethods.stream()
                    .map(PaymentMethodDto::fromDomainToPaymentMethodDto) // 각 요소를 변환
                    .collect(Collectors.toList()); // 변환된 리스트를 수집하여 반환
        } else {
            return Collections.emptyList();
        }
    }

}
