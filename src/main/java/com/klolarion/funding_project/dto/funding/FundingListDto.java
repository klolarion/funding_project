package com.klolarion.funding_project.dto.funding;

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
    private String nickName;
    private Long productId;
    private String productName;
    private Double progress;
    private Long totalFundingAmount;
    private Long currentFundingAmount;
    private String fundingAccount;
    private String status;
    private Integer fundingCategoryCode;
}
