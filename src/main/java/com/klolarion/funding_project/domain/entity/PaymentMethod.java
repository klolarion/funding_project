package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 결제수단
 * */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PaymentMethod extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private int paymentCode;
    private String paymentName;
    private String accountNumber;


    /*테스트를 위한 속성*/
    private Long availableAmount;

}
