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
public class Payment extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private String targetAccount;
    @Column(nullable = false)
    private String paymentAccount;

    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private Long balanceBefore;
    @Column(nullable = false)
    private Long balanceAfter;

    private boolean completed;

    public Payment(Member member, PaymentMethod paymentMethod, String targetAccount, String paymentAccount, Long amount, Long balanceBefore, Long balanceAfter, boolean completed) {
        this.member = member;
        this.paymentMethod = paymentMethod;
        this.targetAccount = targetAccount;
        this.paymentAccount = paymentAccount;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.completed = completed;
    }

    public void setCompletedFalse(){
        this.completed = false;
    }

    public void setCompletedTrue(){
        this.completed = true;
    }
}
