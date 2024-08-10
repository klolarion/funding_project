package com.klolarion.funding_project.domain.entity;


import jakarta.persistence.*;
import lombok.*;

/**
 * 결제목록
 * */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_list_id")
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Long amount;

    private boolean completed;

    public Payment(Member member, PaymentMethod paymentMethod, Long amount, boolean completed) {
        this.member = member;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.completed = completed;
    }
}
