package com.klolarion.funding_project.dto.funding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFundingDto {
    private Long productId;
    private Long GroupId;
    private Long travelId;
    private int fundingCategoryCode;
}
