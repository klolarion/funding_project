package com.klolarion.funding_project.dto.funding;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinFundingDto {
    private Long fundingId;
    private Long amount;
    private Long memberId;
}
