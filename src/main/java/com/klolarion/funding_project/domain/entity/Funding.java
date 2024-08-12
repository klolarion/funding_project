package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Funding extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funding_id")
    private Long fundingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private Long totalFundingAmount;
    @Column(nullable = false)
    private Long currentFundingAmount;

    @Column(nullable = false, unique = true)
    private String fundingAccount;

    private boolean completed;
    private boolean closed;
    private boolean deleted;

    public Funding(Member member, Product product, Group group, Long totalFundingAmount, String fundingAccount) {
        this.member = member;
        this.product = product;
        this.group = group;
        this.totalFundingAmount = totalFundingAmount;
        this.currentFundingAmount = 0L;
        this.fundingAccount = fundingAccount;
        this.completed = false;
        this.closed = false;
        this.deleted = false;
    }

    public boolean addFundingAmount(Long amount){
        if(this.currentFundingAmount + amount <= totalFundingAmount) {
            this.currentFundingAmount += amount;
            return true;
        }else {
            return  false;
        }
    }

    public void completeFunding(){
        this.completed = true;
    }

    public void closeFunding(){
        this.closed = true;
    }

    public void deleteFunding(){
        this.deleted = true;
    }

}
