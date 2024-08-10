package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    @Column(nullable = false)
    private Long totalFundingAmount;
    @Column(nullable = false)
    private Long currentFundingAmount;

    @Column(nullable = false, unique = true)
    private String fundingAccount;

    private boolean completed;
    private boolean closed;
    private boolean deleted;

    public Funding(Member member, Product product, Long currentFundingAmount, String fundingAccount) {
        this.member = member;
        this.product = product;
        this.totalFundingAmount = 0L;
        this.currentFundingAmount = currentFundingAmount;
        this.fundingAccount = fundingAccount;
        this.completed = false;
        this.closed = false;
        this.deleted = false;
    }
}
