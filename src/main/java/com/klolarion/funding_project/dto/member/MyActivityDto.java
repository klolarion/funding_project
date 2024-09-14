package com.klolarion.funding_project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyActivityDto {
    private int myFundingCount;
    private Long fundingAmount;
    private int createdGroupCount;
    private int joinedGroupCount;
}
