package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 결제수단목록
 * */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentMethodList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_list_id")
    private Long paymentMethodListId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean mainPayment;

    private boolean offCd;

    public PaymentMethodList(PaymentMethod paymentMethod, Member member) {
        this.paymentMethod = paymentMethod;
        this.member = member;
        this.mainPayment = false;
    }

    public void deletePaymentMethodList(){
        this.offCd = true;
    }
}
