package com.klolarion.funding_project.dto.payment;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long paymentId;
    private Long memberId;
    private String paymentAccount;
    private String paymentMethodName;
    private String targetAccount;
    private Long amount;
    private Long balanceBefore;
    private Long balanceAfter;
    private String createDate;


    public static PaymentDto fromDomainToPaymentDto(Payment payment, PaymentMethodList fromAccount, PaymentMethodList targetAccount) {
        return new PaymentDto(
                payment.getPaymentId(),
                payment.getMember().getMemberId(),
                fromAccount.getPaymentMethod().getAccountNumber(),
                fromAccount.getPaymentMethod().getPaymentName(),
                targetAccount.getPaymentMethod().getAccountNumber(),
                payment.getAmount(),
                payment.getBalanceBefore(),
                payment.getBalanceAfter(),
                payment.getCreatedDate()
        );
    }
//
//    public static List<PaymentDto> fromDomainListToPaymentDtoList(List<PaymentDto> payments){
//        if (payments != null) {
//            // List<PaymentMethodList>를 List<PaymentMethodListDto>로 변환
//            return payments.stream()
//                    .map(PaymentDto::fromDomainToPaymentDto) // 각 요소를 변환
//                    .collect(Collectors.toList()); // 변환된 리스트를 수집하여 반환
//        } else {
//            return Collections.emptyList();
//        }
//    }
}
