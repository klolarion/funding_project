package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제수단
 * */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(nullable = false)
    private int paymentCode;

    @Column(nullable = false)
    @Size(max = 100)
    private String paymentName;

    @Column(nullable = false, unique = true)
    @Size(max = 100)
    private String accountNumber;

    /*테스트를 위한 속성*/
    private Long availableAmount;


    public PaymentMethod(int paymentCode, String paymentName, String accountNumber, Long availableAmount) {
        this.paymentCode = paymentCode;
        this.paymentName = paymentName;
        this.accountNumber = accountNumber;
        this.availableAmount = availableAmount;
    }

    public boolean deposit(Long amount){
        if(this.availableAmount - amount >= 0){
            this.availableAmount -= amount;
            return true;
        }else {
            return false;
        }
    }

}
