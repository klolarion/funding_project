package com.klolarion.funding_project.dto;

import com.klolarion.funding_project.dto.funding.FundingListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexDto {
    private List<FundingListDto> fundingDtoList;
}
