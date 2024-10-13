package com.klolarion.funding_project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyActivityDto {
    private Long myFundingCount;
    private Long fundingAmount;
    private Long createdGroupCount;
    private Long joinedGroupCount;
}
