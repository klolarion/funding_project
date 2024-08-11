package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 결제수단
 * */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentMethod extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(nullable = false)
    private int paymentCode;

    @Column(nullable = false, unique = true)
    @Size(max = 100)
    private String paymentName;

    @Column(nullable = false, unique = true)
    @Size(max = 100)
    private String accountNumber;

    /*테스트를 위한 속성*/
    private Long availableAmount;

    public boolean deposit(Long amount){
        if(this.availableAmount - amount >= 0){
            this.availableAmount -= amount;
            return true;
        }else {
            return false;
        }
    }

}
