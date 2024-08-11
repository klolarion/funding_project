package com.klolarion.funding_project.dto;


import lombok.Data;

@Data
public class JoinFundingDto {
    private Long fundingId;
    private Long amount;
    private Long memberId;
    private Long groupId;
    private Long paymentMethodListId;
}
