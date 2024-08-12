package com.klolarion.funding_project.dto;

import com.klolarion.funding_project.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundingListDto {
    private Long fundingId;
    private Long memberId;
    private Long groupId;
    private String groupName;
    private String memberName;
    private Long productId;
    private String productName;
    private double progress;
    private Long totalFundingAmount;
    private Long currentFundingAmount;
    private String fundingAccount;
    private String status;

    public FundingListDto(Long fundingId, Long memberId, String memberName, Long productId, String productName,
                         double progress, Long totalFundingAmount, Long currentFundingAmount,
                         String fundingAccount, String status) {
        this.fundingId = fundingId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.productId = productId;
        this.productName = productName;
        this.progress = progress;
        this.totalFundingAmount = totalFundingAmount;
        this.currentFundingAmount = currentFundingAmount;
        this.fundingAccount = fundingAccount;
        this.status = status;
    }

}
