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
    private boolean completed;
    private boolean closed;
    private boolean deleted;

}
