package com.klolarion.funding_project.dto;

import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodListDto {
    private Long paymentMethodListId;
    private Long paymentMethodId;
    private String paymentMethodName;
    private String paymentMethodAccountNumber;
    private Long memberId;
    private boolean mainPayment;
    private Long availableAmount;


    public static PaymentMethodListDto fromDomainToPaymentMethodListDto(PaymentMethodList paymentMethodList) {

        return new PaymentMethodListDto(
                paymentMethodList.getPaymentMethodListId(),
                paymentMethodList.getPaymentMethod().getPaymentMethodId(),
                paymentMethodList.getPaymentMethod().getPaymentName(),
                paymentMethodList.getPaymentMethod().getAccountNumber(),
                paymentMethodList.getMember().getMemberId(),
                paymentMethodList.isMainPayment(),
                paymentMethodList.getPaymentMethod().getAvailableAmount()
        );
    }

    public static List<PaymentMethodListDto> fromDomainListToPaymentMethodListDtoList(List<PaymentMethodList> paymentMethodLists){
        if (paymentMethodLists != null) {
            return paymentMethodLists.stream()
                    .map(PaymentMethodListDto::fromDomainToPaymentMethodListDto) // 각 요소를 변환
                    .collect(Collectors.toList()); // 변환된 리스트를 수집하여 반환
        } else {
            return Collections.emptyList();
        }
    }
}
